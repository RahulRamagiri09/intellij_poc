package com.example.sidebarplugin.GitKB

import com.example.sidebarplugin.GitKB.GitKBResponse.*
import com.example.sidebarplugin.GitInfo
import com.example.sidebarplugin.LanguageDetectUtils
import com.example.sidebarplugin.storage.PersistentState
import com.example.sidebarplugin.utils.ApiUtils
import com.example.sidebarplugin.utils.UIUtils
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.*
import javax.swing.SwingWorker
import com.intellij.openapi.components.ServiceManager

object GitKBActions {
    fun handleGitKBRequest(project: Project, gitKBType: String) {
        val persistentState = ServiceManager.getService(PersistentState::class.java)
        val authToken = persistentState.getAuthToken() ?: ""
        val storedUrl = persistentState.getStoredUrl()?.trimEnd('/') ?: ""
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"

        if (selectedText == "No Code Selected") {
            JOptionPane.showMessageDialog(null, "Please select some code before using the GitKB.")
            return
        }

        val fileExtension = editor?.virtualFile?.extension ?: "NA"
        val language = LanguageDetectUtils.mapFileExtensionToLanguages(fileExtension)

        val gitInfo = GitInfo.getGitInfo(project)
        val projectName = gitInfo?.repositoryName ?: "NA"
        val branchName = gitInfo?.currentBranch ?: "NA"

        val apiUrl = when (gitKBType) {
            "Explain" -> "$storedUrl/gitkb/explain"
            "Get Code" -> "$storedUrl/gitkb/get_code"
            else -> {
                JOptionPane.showMessageDialog(null, "Invalid GitKB type selected.")
                return
            }
        }

        object : SwingWorker<String, Void>() {
            private lateinit var loadingDialog: JDialog

            override fun doInBackground(): String {
                SwingUtilities.invokeLater { loadingDialog = UIUtils.showLoadingDialog { this.cancel(true) } }
                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
            }

            override fun done() {
                if (isCancelled) {
                    SwingUtilities.invokeLater {
                        loadingDialog.dispose()
                        JOptionPane.showMessageDialog(null, "Request was cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE)
                    }
                    return
                }

                try {
                    val response = get()
                    val processedContent: Any = when (gitKBType) {
                        "Explain" -> JsonExplain.extractExplain(response)
                        "Get Code" -> JsonGetCode.extractGetCode(response)
                        else -> "Invalid GitKB type."
                    }

                    SwingUtilities.invokeLater {
                        loadingDialog.dispose()
                        when (processedContent) {
                            is JPanel -> UIUtils.showResponsePanel(project, editor, processedContent, gitKBType)
                            is String -> UIUtils.showSidebarResponse(project, editor, processedContent)
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

