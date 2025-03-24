package com.example.sidebarplugin.Assistant

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


object AssistantActions {
    fun handleAssistantRequest(project: Project, assistantType: String) {
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

        val apiUrl = when (assistantType) {
//            "Add Docstring" -> "http://34.46.36.105:3000/genieapi/assistant/add-docstrings"
//            "Refactor Code" -> "http://34.46.36.105:3000/genieapi/assistant/refactor-code"
//            "Add Error Handler" -> "http://34.46.36.105:3000/genieapi/assistant/add-error-handlng"
//            "Add Logging" -> "http://34.46.36.105:3000/genieapi/assistant/add-logging"
//            "Comment Code" -> "http://34.46.36.105:3000/genieapi/assistant/add-comments"
//            "Explain Code" -> "http://34.46.36.105:3000/genieapi/assistant/explain-code"
//            "Code Generation" -> "http://34.46.36.105:3000/genieapi/assistant/code-generation"
            "Add Docstring" -> "$storedUrl/assistant/add-docstrings"
            "Refactor Code" -> "$storedUrl/assistant/refactor-code"
            "Add Error Handler" -> "$storedUrl/assistant/add-error-handlng"
            "Add Logging" -> "$storedUrl/assistant/add-logging"
            "Comment Code" -> "$storedUrl/assistant/add-comments"
            "Explain Code" -> "$storedUrl/assistant/explain-code"
            "Code Generation" -> "$storedUrl/assistant/code-generation"
            else -> {
                JOptionPane.showMessageDialog(null, "Invalid assistant type selected.")
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
                    val processedContent: Any = when (assistantType) {
                        "Add Docstring" -> JsonDocString.extractDocumentation(response)
                        "Refactor Code" -> JsonRefactor.extractRefactoredCode(response)
                        "Add Error Handler" -> JsonErrorHandler.extractErrorHandlerCode(response)
                        "Add Logging" -> JsonLogging.extractAddLogging(response)
                        "Comment Code" -> JsonCommentCode.extractCommentCode(response)
                        "Explain Code" -> JsonExplainCode.extractExplainCode(response) // Return JPanel for Explain Code
                        "Code Generation" -> JsonCodeGeneration.extractCodeGeneration(response)
                        else -> "Invalid assistant type."
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
