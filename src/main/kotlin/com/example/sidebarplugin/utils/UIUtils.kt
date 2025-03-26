package com.example.sidebarplugin.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.command.WriteCommandAction
import javax.swing.*
import java.awt.BorderLayout
import java.awt.FlowLayout
import com.intellij.openapi.ui.DialogWrapper

object UIUtils {
    fun showLoadingDialog(): JDialog {
        val dialog = JDialog()
        dialog.title = "Processing..."
        dialog.setSize(300, 100)
        dialog.layout = BorderLayout()
        dialog.setLocationRelativeTo(null)
        dialog.isUndecorated = true

        val panel = JPanel(FlowLayout())
        val progressBar = JProgressBar()
        progressBar.isIndeterminate = true
        panel.add(JLabel("Processing request..."))
        panel.add(progressBar)

        dialog.add(panel, BorderLayout.CENTER)
        dialog.isModal = true

        SwingUtilities.invokeLater { dialog.isVisible = true }
        return dialog
    }

    fun showResponseDialog(project: Project, editor: Editor?, newText: String, selectedText: String) {
        if (editor == null) return

        val dialog = object : DialogWrapper(project, true) {
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

                rejectButton.addActionListener { close(CANCEL_EXIT_CODE) }

                buttonPanel.add(acceptButton)
                buttonPanel.add(rejectButton)
                return buttonPanel
            }
        }
        dialog.show()
    }

    fun showResponsePanel(project: Project, editor: Editor?, responsePanel: JPanel) {
        if (editor == null) return

        val dialog = object : DialogWrapper(project, true) {
            init {
                title = "Explanation"
                init()
            }

            override fun createCenterPanel(): JComponent {
                return responsePanel
            }

            override fun createSouthPanel(): JComponent {
                val buttonPanel = JPanel()
                val closeButton = JButton("Close")

                closeButton.addActionListener { close(OK_EXIT_CODE) }

                buttonPanel.add(closeButton)
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
}
