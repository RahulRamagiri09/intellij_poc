package com.example.sidebarplugin

import com.example.sidebarplugin.auth.LoginPanel
import com.example.sidebarplugin.auth.UrlSubmitPanel
import com.example.sidebarplugin.storage.PersistentState
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.intellij.openapi.components.ServiceManager

class SidebarToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val urlState = ServiceManager.getService(PersistentState::class.java)

//        // Temporary code to reset the state for demo purposes
        urlState.resetState()
        println("State reset: storedUrl and authToken set to null")

        val storedUrl = urlState.getStoredUrl()
        val authToken = urlState.getAuthToken()
        println("storedUrl: $storedUrl")
        println("authToken: $authToken")
        println("Checking for stored URL...")
        if (storedUrl != null) {
            println("Stored URL found: $storedUrl")
            if (authToken != null) {
                println("Auth token found: $authToken")
                replacePanelWithSidebarToolWindow(project, toolWindow)
            } else {
                println("No auth token found. Displaying login panel.")
                val loginPanel = LoginPanel(project)
                val content = ContentFactory.getInstance().createContent(loginPanel, "Login", false)
                contentManager.addContent(content)
            }
        } else {
            println("No stored URL found. Displaying URL submission panel.")
            val urlSubmitPanel = UrlSubmitPanel(project)
            val content = ContentFactory.getInstance().createContent(urlSubmitPanel, "URL", false)
            contentManager.addContent(content)
        }
    }

    private fun replacePanelWithSidebarToolWindow(project: Project, toolWindow: ToolWindow) {
        val contentManager = toolWindow.contentManager
        val sidebarToolWindow = SidebarToolWindow(project)
        val content = ContentFactory.getInstance().createContent(sidebarToolWindow, "Sidebar", false)
        contentManager.removeAllContents(true)
        contentManager.addContent(content)
        println("Navigating to sidebar panel")
    }
}