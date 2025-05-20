package com.example.sidebarplugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.ContentFactory
import java.awt.BorderLayout
import java.awt.GridBagLayout
import javax.swing.*
import javax.swing.border.EmptyBorder
import java.awt.FlowLayout
import java.awt.Insets



class SidebarRightToolWindow(private val project: Project, private val toolWindow: ToolWindow) {
    private val contentFactory = ContentFactory.getInstance()
    private val contentPanel = JPanel(BorderLayout())

    init {
        contentPanel.border = EmptyBorder(10, 10, 10, 10)

        // Initial centered message
        val centerPanel = JPanel(GridBagLayout())
        val messageLabel = JLabel("Select an assistant feature to see the response.")
        centerPanel.add(messageLabel)
        contentPanel.add(centerPanel, BorderLayout.CENTER)

        toolWindow.contentManager.removeAllContents(true)
        val content = contentFactory.createContent(contentPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }

    fun updateContent(component: JComponent) {
        SwingUtilities.invokeLater {
            contentPanel.removeAll()
            contentPanel.add(component, BorderLayout.CENTER)
            contentPanel.revalidate()
            contentPanel.repaint()
            toolWindow.show(null)
        }
    }

    fun clearContent() {
        SwingUtilities.invokeLater {
            contentPanel.removeAll()

            val centerPanel = JPanel(GridBagLayout())
            val messageLabel = JLabel("Select an assistant feature to see the response.")
            centerPanel.add(messageLabel)

            contentPanel.add(centerPanel, BorderLayout.CENTER)
            contentPanel.revalidate()
            contentPanel.repaint()
        }
    }

    companion object {
        fun showInSidebar(project: Project, contentText: String, onAccept: () -> Unit, onReject: () -> Unit) {
            val toolWindow = ToolWindowManager.getInstance(project).getToolWindow("Response") ?: return
            val toolWindowUI = SidebarRightToolWindow(project, toolWindow)

            val panel = JPanel(BorderLayout(0, 10)).apply {
                border = EmptyBorder(10, 10, 10, 10)
            }

            val textArea = JTextArea(contentText).apply {
                isEditable = false
                lineWrap = true
                wrapStyleWord = true
                margin = Insets(10, 10, 10, 10)
            }

            panel.add(JScrollPane(textArea), BorderLayout.CENTER)

            val buttonPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
                val accept = JButton("Accept").apply {
                    addActionListener {
                        onAccept()
                        toolWindowUI.clearContent()
                        toolWindow.hide(null)  // <-- hides the sidebar window
                    }
                }
                val reject = JButton("Reject").apply {
                    addActionListener {
                        onReject()
                        toolWindowUI.clearContent()
                        toolWindow.hide(null)  // <-- hides the sidebar window
                    }
                }
                add(accept)
                add(reject)
            }

            panel.add(buttonPanel, BorderLayout.SOUTH)

            toolWindowUI.updateContent(panel)
        }
    }


}