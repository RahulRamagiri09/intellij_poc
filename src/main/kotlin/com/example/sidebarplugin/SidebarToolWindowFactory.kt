package com.example.sidebarplugin

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import javax.swing.SwingUtilities


class SidebarToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
//        val loginPanel = LoginPanel(project)
        val urlSubmitPanel = UrlSubmitPanel(project)
        val content = ContentFactory.getInstance().createContent(urlSubmitPanel, "Login", false)
        contentManager.addContent(content)
    }

}
