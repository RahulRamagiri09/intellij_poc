//package com.example.sidebarplugin.KB
//
//import com.example.sidebarplugin.KB.KBResponse.*
//import com.example.sidebarplugin.GitInfo
//import com.example.sidebarplugin.LanguageDetectUtils
//import com.example.sidebarplugin.storage.PersistentState
//import com.example.sidebarplugin.utils.ApiUtils
//import com.example.sidebarplugin.utils.UIUtils
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.project.Project
//import javax.swing.*
//import javax.swing.SwingWorker
//import com.intellij.openapi.components.ServiceManager
//
//
//object KBActions {
//    fun handleKBRequest(project: Project, KBType: String) {
////        val authToken = AuthTokenStorage.accessToken ?: ""
//        // Retrieve the instance of PersistentState and get authToken
//        val persistentState = ServiceManager.getService(PersistentState::class.java)
//        val authToken = persistentState.getAuthToken() ?: ""
//        val kbStoredUrl = persistentState.getKbStoredUrl()?.trimEnd('/') ?: ""
//        val editor = FileEditorManager.getInstance(project).
//        selectedTextEditor
//        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        if (selectedText == "No Code Selected") {
//            JOptionPane.showMessageDialog(null, "Please select some code before using the KBMS.")
//            return
//        }
//
//        val fileExtension = editor?.virtualFile?.extension ?: "NA"
//        val language = LanguageDetectUtils.mapFileExtensionToLanguages(fileExtension)
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//
//        val apiUrl = when (KBType) {
//            "Get Response From KB" -> "$kbStoredUrl/answer"
//            else -> {
//                JOptionPane.showMessageDialog(null, "Invalid KBMS type selected.")
//                return
//            }
//        }
//
//        object : SwingWorker<String, Void>() {
//            private lateinit var loadingDialog: JDialog
//
//            override fun doInBackground(): String {
//                SwingUtilities.invokeLater { loadingDialog = UIUtils.showLoadingDialog{this.cancel(true)} }
//                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
//            }
//
//            override fun done() {
//                if (isCancelled) {
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose()
//                        // Optionally, show a cancelled message or simply do nothing.
//                        JOptionPane.showMessageDialog(null, "Request was cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE)
//                    }
//                    return
//                }
//                try {
//                    val response = get()
//                    val processedContent: Any = when (KBType) {
//                        "Get Response From KB" -> JsonGetResponseFromKB.extractGetResponseFromKB(response)
//                        else -> "Invalid KBMS type."
//                    }
//
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose()
//                        when (processedContent) {
//                            is JPanel -> UIUtils.showResponsePanel(project, editor, processedContent) // Show JPanel for "Explain Code"
//                            is String -> UIUtils.showResponseDialog(project, editor, processedContent, selectedText) // Show JTextArea for others
//                            else -> JOptionPane.showMessageDialog(null, "Unexpected response type.", "Error", JOptionPane.ERROR_MESSAGE)
//                        }
//                    }
//                } catch (e: Exception) {
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose()
//                        JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
//                    }
//                }
//            }
//        }.execute()
//    }
//}
//
//
//package com.example.sidebarplugin.KB
//
//import com.example.sidebarplugin.KB.KBResponse.*
//import com.example.sidebarplugin.GitInfo
//import com.example.sidebarplugin.LanguageDetectUtils
//import com.example.sidebarplugin.storage.PersistentState
//import com.example.sidebarplugin.utils.ApiUtils
//import com.example.sidebarplugin.utils.UIUtils
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.project.Project
//import javax.swing.*
//import javax.swing.SwingWorker
//import com.intellij.openapi.components.ServiceManager
//
//object KBActions {
//    fun handleKBRequest(project: Project, KBType: String) {
//        val persistentState = ServiceManager.getService(PersistentState::class.java)
//        val authToken = persistentState.getAuthToken() ?: ""
//        val kbStoredUrl = persistentState.getKbStoredUrl()?.trimEnd('/') ?: ""
//        val editor = FileEditorManager.getInstance(project).selectedTextEditor
//        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        if (selectedText == "No Code Selected") {
//            JOptionPane.showMessageDialog(null, "Please select some code before using the KBMS.")
//            return
//        }
//
//        val fileExtension = editor?.virtualFile?.extension ?: "NA"
//        val language = LanguageDetectUtils.mapFileExtensionToLanguages(fileExtension)
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//
//        val apiUrl = when (KBType) {
//            "Get Response From KB" -> "$kbStoredUrl/process-query"
//            else -> {
//                JOptionPane.showMessageDialog(null, "Invalid KBMS type selected.")
//                return
//            }
//        }
//
//        object : SwingWorker<String, Void>() {
//            private lateinit var loadingDialog: JDialog
//
//            override fun doInBackground(): String {
//                SwingUtilities.invokeLater { loadingDialog = UIUtils.showLoadingDialog { this.cancel(true) } }
//                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
//            }
//
//            override fun done() {
//                if (isCancelled) {
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose()
//                        JOptionPane.showMessageDialog(null, "Request was cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE)
//                    }
//                    return
//                }
//
//                try {
//                    val response = get()
//                    val processedContent: Any = when (KBType) {
//                        "Get Response From KB" -> JsonGetResponseFromKB.extractGetResponseFromKB(response)
//                        else -> "Invalid KBMS type."
//                    }
//
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose()
//                        when (processedContent) {
//                            is JPanel -> UIUtils.showResponsePanel(project, editor, processedContent)
//                            is String -> UIUtils.showSidebarResponse(project, editor, processedContent)
//                            else -> JOptionPane.showMessageDialog(null, "Unexpected response type.", "Error", JOptionPane.ERROR_MESSAGE)
//                        }
//                    }
//                } catch (e: Exception) {
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose()
//                        JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
//                    }
//                }
//            }
//        }.execute()
//    }
//}


package com.example.sidebarplugin.KB

import com.example.sidebarplugin.KB.KBResponse.*
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

object KBActions {
    fun handleKBRequest(project: Project, KBType: String) {
        val persistentState = ServiceManager.getService(PersistentState::class.java)
        val authToken = persistentState.getAuthToken() ?: ""
        val kbStoredUrl = persistentState.getKbStoredUrl()?.trimEnd('/') ?: ""
        val editor = FileEditorManager.getInstance(project).selectedTextEditor
        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"

        if (selectedText == "No Code Selected") {
            JOptionPane.showMessageDialog(null, "Please select some code before using the KBMS.")
            return
        }

        val fileExtension = editor?.virtualFile?.extension ?: "NA"
        val language = LanguageDetectUtils.mapFileExtensionToLanguages(fileExtension)

        val gitInfo = GitInfo.getGitInfo(project)
        val projectName = gitInfo?.repositoryName ?: "NA"
        val branchName = gitInfo?.currentBranch ?: "NA"

        val apiUrl = when (KBType) {
            "Get Response From KB" -> "$kbStoredUrl/process-query"
            else -> {
                JOptionPane.showMessageDialog(null, "Invalid KBMS type selected.")
                return
            }
        }

        object : SwingWorker<String, Void>() {
            private lateinit var loadingDialog: JDialog

            override fun doInBackground(): String {
                val latch = java.util.concurrent.CountDownLatch(1)
                SwingUtilities.invokeAndWait {
                    loadingDialog = UIUtils.showLoadingDialog { this.cancel(true) }
                    latch.countDown()
                }
                latch.await()
                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
            }

            override fun done() {
                SwingUtilities.invokeLater {
                    if (::loadingDialog.isInitialized) {
                        loadingDialog.dispose()
                    }

                    if (isCancelled) {
                        JOptionPane.showMessageDialog(null, "Request was cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE)
                        return@invokeLater
                    }

                    try {
                        val response = get()
                        val processedContent: Any = when (KBType) {
                            "Get Response From KB" -> JsonGetResponseFromKB.extractGetResponseFromKB(response)
                            else -> "Invalid KBMS type."
                        }

                        when (processedContent) {
                            is JPanel -> UIUtils.showResponsePanel(project, editor, processedContent)
                            is String -> UIUtils.showSidebarResponse(project, editor, processedContent)
                            else -> JOptionPane.showMessageDialog(null, "Unexpected response type.", "Error", JOptionPane.ERROR_MESSAGE)
                        }
                    } catch (e: Exception) {
                        JOptionPane.showMessageDialog(null, "Error: \${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
                    }
                }
            }
        }.execute()
    }
}

