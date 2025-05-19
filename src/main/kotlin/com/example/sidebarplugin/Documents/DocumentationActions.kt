package com.example.sidebarplugin.Documents

import com.example.sidebarplugin.ui.AssistantToolWindow
import com.example.sidebarplugin.ui.createRepoDocumentationUI
import com.intellij.openapi.project.Project

object DocumentationActions {
    fun handleDocumentationRequest(project: Project, item: String) {
        when (item) {
            "Repo Documentation" -> {
                val panel = createRepoDocumentationUI(
                    onAccept = { println("User accepted Repo Documentation") },
                    onReject = { println("User rejected Repo Documentation") }
                )

                val toolWindow = com.intellij.openapi.wm.ToolWindowManager
                    .getInstance(project)
                    .getToolWindow("Response")

                if (toolWindow != null) {
                    val toolWindowUI = AssistantToolWindow(project, toolWindow)
                    toolWindowUI.updateContent(panel)
                }
            }
            else -> {
                println("Unknown documentation item: $item")
            }
        }
    }
}
