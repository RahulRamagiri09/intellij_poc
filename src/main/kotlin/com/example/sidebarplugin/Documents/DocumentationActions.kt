package com.example.sidebarplugin.Documents

import com.example.sidebarplugin.ui.SidebarRightToolWindow
import com.example.sidebarplugin.ui.createRepoDocumentationUI
import com.intellij.openapi.project.Project

object DocumentationActions {
    fun handleDocumentationRequest(project: Project, item: String) {
        when (item) {
            "Repo Documentation" -> {
                val panel = createRepoDocumentationUI(

                )

                val toolWindow = com.intellij.openapi.wm.ToolWindowManager
                    .getInstance(project)
                    .getToolWindow("Response")

                if (toolWindow != null) {
                    val toolWindowUI = SidebarRightToolWindow(project, toolWindow)
                    toolWindowUI.updateContent(panel)
                }
            }
            else -> {
                println("Unknown documentation item: $item")
            }
        }
    }
}
