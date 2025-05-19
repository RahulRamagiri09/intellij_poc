package com.example.sidebarplugin.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

class SidebarRightToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        // Create instance of the tool window and initialize it
        val assistantToolWindow = SidebarRightToolWindow(project, toolWindow)

        // You can store this instance if needed globally
        // Or make AssistantToolWindow a singleton if required
    }
}
