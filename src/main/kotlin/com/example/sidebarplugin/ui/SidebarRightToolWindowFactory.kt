package com.example.sidebarplugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class AssistantToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // Create instance of the tool window and initialize it
        val assistantToolWindow = AssistantToolWindow(project, toolWindow)

        // You can store this instance if needed globally
        // Or make AssistantToolWindow a singleton if required
    }
}
