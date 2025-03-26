package com.example.sidebarplugin.Review

import com.example.sidebarplugin.Assistant.AssistantUtils
import com.example.sidebarplugin.AssistantResponse.*
import com.example.sidebarplugin.GitInfo
import com.example.sidebarplugin.storage.PersistentState
import com.example.sidebarplugin.utils.ApiUtils
import com.example.sidebarplugin.utils.UIUtils
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.*
import javax.swing.SwingWorker
import com.intellij.openapi.components.ServiceManager


object ReviewActions {
    fun handleReviewRequest(project: Project, reviewType: String) {
//        val authToken = AuthTokenStorage.accessToken ?: ""
        // Retrieve the instance of PersistentState and get authToken
        val persistentState = ServiceManager.getService(PersistentState::class.java)
        val authToken = persistentState.getAuthToken() ?: ""
        val storedUrl = persistentState.getStoredUrl()?.trimEnd('/') ?: ""
        val editor = FileEditorManager.getInstance(project).
        selectedTextEditor
        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"

        if (selectedText == "No Code Selected") {
            JOptionPane.showMessageDialog(null, "Please select some code before using the assistant.")
            return
        }

        val fileExtension = editor?.virtualFile?.extension ?: "NA"
        val language = AssistantUtils.mapFileExtensionToLanguages(fileExtension)

        val gitInfo = GitInfo.getGitInfo(project)
        val projectName = gitInfo?.repositoryName ?: "NA"
        val branchName = gitInfo?.currentBranch ?: "NA"

        val apiUrl = when (reviewType) {
            "Overall Review" -> "$storedUrl/review/review"
            else -> {
                JOptionPane.showMessageDialog(null, "Invalid Review type selected.")
                return
            }
        }

        object : SwingWorker<String, Void>() {
            private lateinit var loadingDialog: JDialog

            override fun doInBackground(): String {
                SwingUtilities.invokeLater { loadingDialog = UIUtils.showLoadingDialog() }
                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
            }

            override fun done() {
                try {
                    val response = get()
                    val processedContent: Any = when (reviewType) {
                        "Overall Review" -> JsonOverallReview.extractOverallReview(response)

                        else -> "Invalid review type."
                    }

                    SwingUtilities.invokeLater {
                        loadingDialog.dispose()
                        when (processedContent) {
                            is JPanel -> UIUtils.showResponsePanel(project, editor, processedContent) // Show JPanel for "Explain Code"
                            is String -> UIUtils.showResponseDialog(project, editor, processedContent, selectedText) // Show JTextArea for others
                            else -> JOptionPane.showMessageDialog(null, "Unexpected response type.", "Error", JOptionPane.ERROR_MESSAGE)
                        }
                    }
                } catch (e: Exception) {
                    SwingUtilities.invokeLater {
                        loadingDialog.dispose()
                        JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
                    }
                }
            }
        }.execute()
    }
}
