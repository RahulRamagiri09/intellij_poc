package com.example.sidebarplugin.Assistant
import java.util.concurrent.atomic.AtomicReference

import com.example.sidebarplugin.Assistant.AssistantResponse.*
import com.example.sidebarplugin.GitInfo
import com.example.sidebarplugin.LanguageDetectUtils
import com.example.sidebarplugin.storage.PersistentState
import com.example.sidebarplugin.utils.ApiUtils
import com.example.sidebarplugin.utils.UIUtils
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.*
import kotlin.concurrent.thread
import com.intellij.openapi.components.ServiceManager
import java.util.concurrent.atomic.AtomicBoolean
import org.json.JSONObject

object AssistantActions {
    fun handleAssistantRequest(project: Project, assistantType: String) {
        val persistentState = ServiceManager.getService(PersistentState::class.java)
        val authToken = persistentState.getAuthToken() ?: ""
        val storedUrl = persistentState.getStoredUrl()?.trimEnd('/') ?: ""
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"

        if (selectedText == "No Code Selected") {
            JOptionPane.showMessageDialog(null, "Please select some code before using the assistant.")
            return
        }

        val fileExtension = editor?.virtualFile?.extension ?: "NA"
        val language = LanguageDetectUtils.mapFileExtensionToLanguages(fileExtension)

        val gitInfo = GitInfo.getGitInfo(project)
        val projectName = gitInfo?.repositoryName ?: "NA"
        val branchName = gitInfo?.currentBranch ?: "NA"

        val apiUrl = when (assistantType) {
            "Add Docstring" -> "$storedUrl/assistant/add-docstrings"
            "Refactor Code" -> "$storedUrl/assistant/refactor-code"
            "Add Error Handler" -> "$storedUrl/assistant/add-error-handlng"
            "Add Logging" -> "$storedUrl/assistant/add-logging"
            "Comment Code" -> "$storedUrl/assistant/add-comments"
            "Explain Code" -> "$storedUrl/assistant/explain-code"
            "Code Generation" -> "$storedUrl/assistant/code-generation"
            "Unit Test Code" -> "$storedUrl/assistant/unittest-code"
            "Filewise Unit Test Code" -> "$storedUrl/assistant/file-testCases"
            else -> {
                JOptionPane.showMessageDialog(null, "Invalid assistant type selected.")
                return
            }
        }

        if (assistantType == "Filewise Unit Test Code") {
            val isCancelled = AtomicBoolean(false)
            var jobId: String? = null

            val loadingDialog = UIUtils.showLoadingDialog {
                // Cancel callback when user clicks Cancel button
                isCancelled.set(true)
                val id = jobId
                if (id != null) {
                    thread {
                        try {
                            val cancelUrl = "$storedUrl/assistant/file-testCases/cancel"

                            val cancelResponse = ApiUtils.sendCancelJobRequest(cancelUrl, id, authToken)
                            SwingUtilities.invokeLater {
                                val message = try {
                                    val json = JSONObject(cancelResponse)
                                    json.optString("message", cancelResponse)
                                } catch (e: Exception) {
                                    cancelResponse
                                }
                                JOptionPane.showMessageDialog(null, message)
                            }
                        } catch (e: Exception) {
                            SwingUtilities.invokeLater {
                                JOptionPane.showMessageDialog(null, "Failed to cancel job: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
                            }
                        }
                    }
                }
            }

            val cancellationMessage = AtomicReference<String?>(null)

            thread {
                try {
                    val jobResponse = ApiUtils.sendJobRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
                    jobId = JsonFilewiseUnitTestCode.extractJobID(jobResponse)

                    val id = jobId
                    if (id == null || id.startsWith("Error") || id == "JobID not found") {
                        SwingUtilities.invokeLater {
                            loadingDialog.dispose()
                            JOptionPane.showMessageDialog(null, id ?: "JobID not found", "Error", JOptionPane.ERROR_MESSAGE)
                        }
                        return@thread
                    }

                    val statusUrl = "$storedUrl/assistant/file-testCases/status"
                    val results = JsonFilewiseUnitTestCode.pollForResults(statusUrl, id, authToken) {
                        isCancelled.get()
                    }

                    SwingUtilities.invokeLater {
                        loadingDialog.dispose()
                        if (isCancelled.get()) {
                            // Only show cancellation message if API gave one
                            val cancelMsg = cancellationMessage.get()
                            if (!cancelMsg.isNullOrBlank()) {
                                JOptionPane.showMessageDialog(null, cancelMsg, "Cancelled", JOptionPane.INFORMATION_MESSAGE)
                            }
                        } else if (results.startsWith("Timeout") || results.startsWith("Error")) {
                            JOptionPane.showMessageDialog(null, results, "Error", JOptionPane.ERROR_MESSAGE)
                        } else {
                            val panel = JsonFilewiseUnitTestCode.renderResultsPanel(results, language)
                            UIUtils.showResponsePanel(project, editor, panel)
                        }
                    }
                } catch (e: Exception) {
                    SwingUtilities.invokeLater {
                        loadingDialog.dispose()
                        JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
                    }
                }
            }

        } else {
            object : SwingWorker<String, Void>() {
                private lateinit var loadingDialog: JDialog
                private val isCancelled = AtomicBoolean(false)

                override fun doInBackground(): String {
                    SwingUtilities.invokeLater {
                        loadingDialog = UIUtils.showLoadingDialog {
                            isCancelled.set(true)
                            this.cancel(true)
                        }
                    }
                    return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
                }

                override fun done() {
                    if (isCancelled.get()) {
                        SwingUtilities.invokeLater {
                            loadingDialog.dispose()
                            JOptionPane.showMessageDialog(null, "Request was cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE)
                        }
                        return
                    }

                    try {
                        val response = get()
                        val processedContent: Any = when (assistantType) {
                            "Add Docstring" -> JsonDocString.extractDocumentation(response)
                            "Refactor Code" -> JsonRefactor.extractRefactoredCode(response)
                            "Add Error Handler" -> JsonErrorHandler.extractErrorHandlerCode(response)
                            "Add Logging" -> JsonLogging.extractAddLogging(response)
                            "Comment Code" -> JsonCommentCode.extractCommentCode(response)
                            "Explain Code" -> JsonExplainCode.extractExplainCode(response)
                            "Code Generation" -> JsonCodeGeneration.extractCodeGeneration(response)
                            "Unit Test Code" -> JsonUnitTestCode.extractUnitTestCode(response)
                            else -> "Invalid assistant type."
                        }

                        SwingUtilities.invokeLater {
                            loadingDialog.dispose()
                            when (processedContent) {
                                is JPanel -> UIUtils.showResponsePanel(project, editor, processedContent)
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
}
