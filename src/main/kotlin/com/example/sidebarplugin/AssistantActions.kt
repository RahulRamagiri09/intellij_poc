package com.example.sidebarplugin

//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.project.Project

import kotlinx.serialization.json.*

//import com.intellij.openapi.command.WriteCommandAction
//import com.intellij.openapi.editor.Editor
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.ui.DialogWrapper
import kotlinx.serialization.json.*
//import java.awt.BorderLayout
import javax.swing.*
//
//object AssistantActions {
//
//    fun handleAssistantRequest(project: Project, assistantType: String): String {
//        val authToken = AuthTokenStorage.accessToken ?: ""
//        val editor = FileEditorManager.getInstance(project).selectedTextEditor
//        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        if (selectedText == "No Code Selected") {
//            return "Please select some code before using the assistant."
//        }
//
//        val fileExtension = editor?.virtualFile?.extension ?: "NA"
//        val language = mapFileExtensionToLanguages(fileExtension)
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//
//        val apiUrl = when (assistantType) {
//            "Doc String" -> "http://34.57.32.181:3000/fastapi/assistant/add-docstrings"
//            "Refactor" -> "http://34.57.32.181:3000/fastapi/review/overall"
//            else -> return "Invalid assistant type selected."
//        }
//
//        val response = ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
//        val documentationAdded = extractDocumentation(response)
//
//        showResponseDialog(project, editor, documentationAdded, selectedText)
//
//        return "Assistant completed. Please choose an action."
//    }
//
//    private fun extractDocumentation(response: String): String {
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            jsonElement["documentationAdded"]?.jsonPrimitive?.content ?: "No documentation added."
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message}"
//        }
//    }
//
//    private fun showResponseDialog(project: Project, editor: Editor?, newText: String, selectedText: String) {
//        if (editor == null) return
//
//        val dialog = object : DialogWrapper(true) {
//            init {
//                title = "Assistant Response"
//                init()
//            }
//
//            override fun createCenterPanel(): JComponent {
//                val panel = JPanel(BorderLayout())
//                val textArea = JTextArea(newText)
//                textArea.isEditable = false
//                panel.add(JScrollPane(textArea), BorderLayout.CENTER)
//                return panel
//            }
//
//            override fun createSouthPanel(): JComponent {
//                val buttonPanel = JPanel()
//                val acceptButton = JButton("Accept")
//                val rejectButton = JButton("Reject")
//
//                acceptButton.addActionListener {
//                    replaceSelectedText(editor, selectedText, newText)
//                    close(OK_EXIT_CODE)
//                }
//
//                rejectButton.addActionListener {
//                    close(CANCEL_EXIT_CODE)
//                }
//
//                buttonPanel.add(acceptButton)
//                buttonPanel.add(rejectButton)
//                return buttonPanel
//            }
//        }
//        dialog.show()
//    }
//
//    private fun replaceSelectedText(editor: Editor, oldText: String, newText: String) {
//        val project = editor.project ?: return
//        val document = editor.document
//        val selectionModel = editor.selectionModel
//
//        WriteCommandAction.runWriteCommandAction(project) {
//            document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, newText)
//        }
//    }
//
//    private fun mapFileExtensionToLanguages(fileExtension: String): String {
//        return when (fileExtension.lowercase()) {
//            "py" -> "Python"
//            "java" -> "Java"
//            "kt" -> "Kotlin"
//            "js" -> "JavaScript"
//            "ts" -> "TypeScript"
//            "cpp", "cxx", "cc" -> "C++"
//            "c" -> "C"
//            "cs" -> "C#"
//            "rb" -> "Ruby"
//            "php" -> "PHP"
//            "swift" -> "Swift"
//            "go" -> "Go"
//            "rs" -> "Rust"
//            "html" -> "HTML"
//            "css" -> "CSS"
//            "sh" -> "Shell Script"
//            "json" -> "JSON"
//            "yaml", "yml" -> "YAML"
//            "xml" -> "XML"
//            "sql" -> "SQL"
//            else -> "Unknown"
//        }
//    }
//}

import kotlinx.serialization.json.*
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.*
import java.awt.BorderLayout
import java.awt.FlowLayout
//import javax.swing.SwingWorker
//
//object AssistantActions {
//
//    fun handleAssistantRequest(project: Project, assistantType: String): String {
//        val authToken = AuthTokenStorage.accessToken ?: ""
//        val editor = FileEditorManager.getInstance(project).selectedTextEditor
//        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        if (selectedText == "No Code Selected") {
//            return "Please select some code before using the assistant."
//        }
//
//        val fileExtension = editor?.virtualFile?.extension ?: "NA"
//        val language = mapFileExtensionToLanguages(fileExtension)
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//
//        val apiUrl = when (assistantType) {
//            "Doc String" -> "http://34.57.32.181:3000/fastapi/assistant/add-docstrings"
//            "Refactor" -> "http://34.57.32.181:3000/fastapi/review/overall"
//            else -> return "Invalid assistant type selected."
//        }
//
//        // Show Loader
//        val loadingDialog = showLoadingDialog()
//
//        // Run API call in the background
//        object : SwingWorker<String, Void>() {
//            override fun doInBackground(): String {
//                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
//            }
//
//            override fun done() {
//                loadingDialog.dispose() // Close the loader
//                val response = get() // Get the API response
//                val documentationAdded = extractDocumentation(response)
//                showResponseDialog(project, editor, documentationAdded, selectedText)
//            }
//        }.execute()
//
//        return "Assistant processing..."
//    }
//
//    private fun showLoadingDialog(): JDialog {
//        val dialog = JDialog()
//        dialog.title = "Processing..."
//        dialog.setSize(300, 100)
//        dialog.layout = BorderLayout()
//        dialog.setLocationRelativeTo(null) // Center the dialog
//
//        val panel = JPanel(FlowLayout())
//        val progressBar = JProgressBar()
//        progressBar.isIndeterminate = true
//        panel.add(JLabel("Processing request..."))
//        panel.add(progressBar)
//
//        dialog.add(panel, BorderLayout.CENTER)
//        dialog.isModal = true // Prevent user interaction with the main window
//        dialog.isVisible = true
//        return dialog
//    }
//
//    private fun extractDocumentation(response: String): String {
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            jsonElement["documentationAdded"]?.jsonPrimitive?.content ?: "No documentation added."
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message}"
//        }
//    }
//
//    private fun showResponseDialog(project: Project, editor: Editor?, newText: String, selectedText: String) {
//        if (editor == null) return
//
//        val dialog = object : DialogWrapper(true) {
//            init {
//                title = "Assistant Response"
//                init()
//            }
//
//            override fun createCenterPanel(): JComponent {
//                val panel = JPanel(BorderLayout())
//                val textArea = JTextArea(newText)
//                textArea.isEditable = false
//                panel.add(JScrollPane(textArea), BorderLayout.CENTER)
//                return panel
//            }
//
//            override fun createSouthPanel(): JComponent {
//                val buttonPanel = JPanel()
//                val acceptButton = JButton("Accept")
//                val rejectButton = JButton("Reject")
//
//                acceptButton.addActionListener {
//                    replaceSelectedText(editor, selectedText, newText)
//                    close(OK_EXIT_CODE)
//                }
//
//                rejectButton.addActionListener {
//                    close(CANCEL_EXIT_CODE)
//                }
//
//                buttonPanel.add(acceptButton)
//                buttonPanel.add(rejectButton)
//                return buttonPanel
//            }
//        }
//        dialog.show()
//    }
//
//    private fun replaceSelectedText(editor: Editor, oldText: String, newText: String) {
//        val project = editor.project ?: return
//        val document = editor.document
//        val selectionModel = editor.selectionModel
//
//        WriteCommandAction.runWriteCommandAction(project) {
//            document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, newText)
//        }
//    }
//
//    private fun mapFileExtensionToLanguages(fileExtension: String): String {
//        return when (fileExtension.lowercase()) {
//            "py" -> "Python"
//            "java" -> "Java"
//            "kt" -> "Kotlin"
//            "js" -> "JavaScript"
//            "ts" -> "TypeScript"
//            "cpp", "cxx", "cc" -> "C++"
//            "c" -> "C"
//            "cs" -> "C#"
//            "rb" -> "Ruby"
//            "php" -> "PHP"
//            "swift" -> "Swift"
//            "go" -> "Go"
//            "rs" -> "Rust"
//            "html" -> "HTML"
//            "css" -> "CSS"
//            "sh" -> "Shell Script"
//            "json" -> "JSON"
//            "yaml", "yml" -> "YAML"
//            "xml" -> "XML"
//            "sql" -> "SQL"
//            else -> "Unknown"
//        }
//    }
//}


import javax.swing.*
import java.awt.*
import javax.swing.SwingWorker

object AssistantActions {

//    fun handleAssistantRequest(project: Project, assistantType: String): String {
//        val authToken = AuthTokenStorage.accessToken ?: ""
//        val editor = FileEditorManager.getInstance(project).selectedTextEditor
//        val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        if (selectedText == "No Code Selected") {
//            return "Please select some code before using the assistant."
//        }
//
//        val fileExtension = editor?.virtualFile?.extension ?: "NA"
//        val language = mapFileExtensionToLanguages(fileExtension)
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//
//        val apiUrl = when (assistantType) {
//            "Doc String" -> "http://34.57.32.181:3000/fastapi/assistant/add-docstrings"
//            "Refactor" -> "http://34.57.32.181:3000/fastapi/review/overall"
//            else -> return "Invalid assistant type selected."
//        }
//
//        // Show Loader (Modal)
//        val loadingDialog = showLoadingDialog()
//
//        // Run API call in background
//        object : SwingWorker<String, Void>() {
//            override fun doInBackground(): String {
//                return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
//            }
//
//            override fun done() {
//                try {
//                    val response = get() // Get API response
//                    val documentationAdded = extractDocumentation(response)
//
//                    SwingUtilities.invokeLater {
//                        loadingDialog.dispose() // Ensure loader closes
//                        showResponseDialog(project, editor, documentationAdded, selectedText)
//                    }
//                } catch (e: Exception) {
//                    loadingDialog.dispose() // Ensure loader closes on failure
//                    JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
//                }
//            }
//        }.execute()
//
//        return "Assistant processing completed"
//    }




//    working with the 2 dialog box
//fun handleAssistantRequest(project: Project, assistantType: String) {
//    val authToken = AuthTokenStorage.accessToken ?: ""
//    val editor = FileEditorManager.getInstance(project).selectedTextEditor
//    val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//    if (selectedText == "No Code Selected") {
//        JOptionPane.showMessageDialog(null, "Please select some code before using the assistant.")
//        return
//    }
//
//    val fileExtension = editor?.virtualFile?.extension ?: "NA"
//    val language = mapFileExtensionToLanguages(fileExtension)
//
//    val gitInfo = GitInfo.getGitInfo(project)
//    val projectName = gitInfo?.repositoryName ?: "NA"
//    val branchName = gitInfo?.currentBranch ?: "NA"
//
//    val apiUrl = when (assistantType) {
//        "Doc String" -> "http://34.57.32.181:3000/fastapi/assistant/add-docstrings"
//        "Refactor" -> "http://34.57.32.181:3000/fastapi/review/overall"
//        else -> {
//            JOptionPane.showMessageDialog(null, "Invalid assistant type selected.")
//            return
//        }
//    }
//
//    // Show Loader (Modal)
//    val loadingDialog = showLoadingDialog()
//
//    // Run API call in background
//    object : SwingWorker<String, Void>() {
//        override fun doInBackground(): String {
//            return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
//        }
//
//        override fun done() {
//            try {
//                val response = get() // Get API response
//                val documentationAdded = extractDocumentation(response)
//
//                SwingUtilities.invokeLater {
//                    loadingDialog.dispose() // Ensure loader closes
//                    showResponseDialog(project, editor, documentationAdded, selectedText)
//                }
//            } catch (e: Exception) {
//                loadingDialog.dispose() // Ensure loader closes on failure
//                JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
//            }
//        }
//    }.execute()
//}
fun handleAssistantRequest(project: Project, assistantType: String) {
    val authToken = AuthTokenStorage.accessToken ?: ""
    val editor = FileEditorManager.getInstance(project).selectedTextEditor
    val selectedText = editor?.selectionModel?.selectedText ?: "No Code Selected"

    if (selectedText == "No Code Selected") {
        JOptionPane.showMessageDialog(null, "Please select some code before using the assistant.")
        return
    }

    val fileExtension = editor?.virtualFile?.extension ?: "NA"
    val language = mapFileExtensionToLanguages(fileExtension)

    val gitInfo = GitInfo.getGitInfo(project)
    val projectName = gitInfo?.repositoryName ?: "NA"
    val branchName = gitInfo?.currentBranch ?: "NA"

    val apiUrl = when (assistantType) {
        "Doc String" -> "http://34.57.32.181:3000/fastapi/assistant/add-docstrings"
        "Refactor" -> "http://34.57.32.181:3000/fastapi/assistant/refactor-code"
        else -> {
            JOptionPane.showMessageDialog(null, "Invalid assistant type selected.")
            return
        }
    }

    object : SwingWorker<String, Void>() {
        private lateinit var loadingDialog: JDialog  // Declare here, initialize later

        override fun doInBackground(): String {
            SwingUtilities.invokeLater {
                loadingDialog = showLoadingDialog()  // Show the dialog AFTER starting the request
            }
            return ApiUtils.sendReviewRequest(apiUrl, selectedText, language, authToken, projectName, branchName)
        }

        override fun done() {
            try {
                val response = get() // Get API response
                val documentationAdded = extractDocumentation(response)

                SwingUtilities.invokeLater {
                    loadingDialog.dispose() // Close the loading dialog
                    showResponseDialog(project, editor, documentationAdded, selectedText)
                }
            } catch (e: Exception) {
                SwingUtilities.invokeLater {
                    loadingDialog.dispose() // Ensure loader closes on failure
                    JOptionPane.showMessageDialog(null, "Error: ${e.message}", "API Error", JOptionPane.ERROR_MESSAGE)
                }
            }
        }
    }.execute()
}

    private fun showLoadingDialog(): JDialog {
        val dialog = JDialog()
        dialog.title = "Processing..."
        dialog.setSize(300, 100)
        dialog.layout = BorderLayout()
        dialog.setLocationRelativeTo(null) // Center the dialog
        dialog.isUndecorated = true // Remove window decorations

        val panel = JPanel(FlowLayout())
        val progressBar = JProgressBar()
        progressBar.isIndeterminate = true
        panel.add(JLabel("Processing request..."))
        panel.add(progressBar)

        dialog.add(panel, BorderLayout.CENTER)
        dialog.isModal = true

        // Show loader in a separate thread
        SwingUtilities.invokeLater { dialog.isVisible = true }

        return dialog
    }

    private fun extractDocumentation(response: String): String {
        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            jsonElement["documentationAdded"]?.jsonPrimitive?.content ?: "No documentation added."
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message}"
        }
    }

    private fun showResponseDialog(project: Project, editor: Editor?, newText: String, selectedText: String) {
        if (editor == null) return

        val dialog = object : DialogWrapper(true) {
            init {
                title = "Assistant Response"
                init()
            }

            override fun createCenterPanel(): JComponent {
                val panel = JPanel(BorderLayout())
                val textArea = JTextArea(newText)
                textArea.isEditable = false
                panel.add(JScrollPane(textArea), BorderLayout.CENTER)
                return panel
            }

            override fun createSouthPanel(): JComponent {
                val buttonPanel = JPanel()
                val acceptButton = JButton("Accept")
                val rejectButton = JButton("Reject")

                acceptButton.addActionListener {
                    replaceSelectedText(editor, selectedText, newText)
                    close(OK_EXIT_CODE)
                }

                rejectButton.addActionListener {
                    close(CANCEL_EXIT_CODE)
                }

                buttonPanel.add(acceptButton)
                buttonPanel.add(rejectButton)
                return buttonPanel
            }
        }
        dialog.show()
    }

    private fun replaceSelectedText(editor: Editor, oldText: String, newText: String) {
        val project = editor.project ?: return
        val document = editor.document
        val selectionModel = editor.selectionModel

        WriteCommandAction.runWriteCommandAction(project) {
            document.replaceString(selectionModel.selectionStart, selectionModel.selectionEnd, newText)
        }
    }

    private fun mapFileExtensionToLanguages(fileExtension: String): String {
        return when (fileExtension.lowercase()) {
            "py" -> "Python"
            "java" -> "Java"
            "kt" -> "Kotlin"
            "js" -> "JavaScript"
            "ts" -> "TypeScript"
            "cpp", "cxx", "cc" -> "C++"
            "c" -> "C"
            "cs" -> "C#"
            "rb" -> "Ruby"
            "php" -> "PHP"
            "swift" -> "Swift"
            "go" -> "Go"
            "rs" -> "Rust"
            "html" -> "HTML"
            "css" -> "CSS"
            "sh" -> "Shell Script"
            "json" -> "JSON"
            "yaml", "yml" -> "YAML"
            "xml" -> "XML"
            "sql" -> "SQL"
            else -> "Unknown"
        }
    }
}
