//package com.example.sidebarplugin
//
//import com.example.sidebarplugin.Assistant.AssistantItems
//import com.example.sidebarplugin.Review.ReviewItems
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.ui.SimpleToolWindowPanel
//import com.intellij.ui.components.JBList
//import com.intellij.ui.components.JBScrollPane
//import javax.swing.*
//import java.awt.*
//
//class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
//    private val mainPanel: JPanel = JPanel()
//
//    init {
//        mainPanel.layout = BorderLayout()
//        addProjectNameHeader()
//        addDropdownMenu()
//        setContent(mainPanel)
//    }
//
//    private fun addProjectNameHeader() {
//        val projectNameLabel = JLabel("Project: ${project.name}")
//        projectNameLabel.font = Font("Arial", Font.BOLD, 14)
//        projectNameLabel.horizontalAlignment = SwingConstants.CENTER
//
//        mainPanel.add(projectNameLabel, BorderLayout.NORTH)
//    }
//
////    private fun addDropdownMenu() {
////        val categories = listOf("Assistant", "Review")
////        val categoryList = JBList(categories)
////        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
////
////        val dropdownPanel = JPanel()
////        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
////
////        // Auth section
////        val authDropdown = DropDownMenuAuth.createDropdownMenu("Auth", mainPanel, project)
////
////        dropdownPanel.add(authDropdown)
////
////        // Assistant section
////        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
////        dropdownPanel.add(assistantDropdown)
////
////        // Review section
////        val reviewDropdown = DropDownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
////        dropdownPanel.add(reviewDropdown)
////
////        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
////    }
//
//    private fun addDropdownMenu() {
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Auth category
//        val authCategory = JLabel("Auth") // Keeping it as a category
//        authCategory.font = Font("Arial", Font.BOLD, 12)
//        dropdownPanel.add(authCategory)
//        val authDropdown = DropDownMenuAuth.createDropdownMenu("Auth", mainPanel, project)
//        dropdownPanel.add(authDropdown)
//
//        // Assistant category
//        val assistantCategory = JLabel("Assistant")
//        assistantCategory.font = Font("Arial", Font.BOLD, 12)
//        dropdownPanel.add(assistantCategory)
//        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
//        dropdownPanel.add(assistantDropdown)
//
//        // Review category
//        val reviewCategory = JLabel("Review")
//        reviewCategory.font = Font("Arial", Font.BOLD, 12)
//        dropdownPanel.add(reviewCategory)
//        val reviewDropdown = DropDownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
//        dropdownPanel.add(reviewDropdown)
//
//        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
//    }
//
//    private fun createDropdownMenu(title: String, items: List<String>): JPanel {
//        val panel = JPanel()
//        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
//        val label = JLabel(title)
//        label.font = Font("Arial", Font.BOLD, 12)
//        panel.add(label)
//
//        val list = JBList(items)
//        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
//        panel.add(JBScrollPane(list))
//
//        return panel
//    }
//}
//
////package com.example.sidebarplugin
////
////import com.example.sidebarplugin.Assistant.AssistantItems
////import com.example.sidebarplugin.Review.ReviewItems
////import com.example.sidebarplugin.auth.LoginPanel
////import com.example.sidebarplugin.storage.PersistentState
////import com.intellij.openapi.project.Project
////import com.intellij.openapi.ui.SimpleToolWindowPanel
////import com.intellij.ui.components.JBList
////import com.intellij.ui.components.JBScrollPane
////import javax.swing.*
////import java.awt.*
////
////class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
////    private val mainPanel: JPanel = JPanel()
//////    private val urlState = PersistentState.getInstance() // Retrieve stored authentication state
////    private val urlState = com.intellij.openapi.components.ServiceManager.getService(PersistentState::class.java)
////
////
////    init {
////        mainPanel.layout = BorderLayout()
////
////        if (urlState.getAuthToken().isNullOrEmpty()) {
////            // Show LoginPanel if not authenticated
////            mainPanel.add(LoginPanel(project), BorderLayout.CENTER)
////        } else {
////            setupSidebarContent()
////        }
////
////        setContent(mainPanel)
////    }
////
////    private fun setupSidebarContent() {
////        addProjectNameHeader()
////        addDropdownMenu()
////    }
////
////    private fun addProjectNameHeader() {
////        val projectNameLabel = JLabel("Project: ${project.name}")
////        projectNameLabel.font = Font("Arial", Font.BOLD, 14)
////        projectNameLabel.horizontalAlignment = SwingConstants.CENTER
////        mainPanel.add(projectNameLabel, BorderLayout.NORTH)
////    }
////
////    private fun addDropdownMenu() {
////        val categories = listOf("Assistant", "Review")
////        val categoryList = JBList(categories)
////        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
////
////        val dropdownPanel = JPanel()
////        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
////
////        // Auth section
////        val authDropdown = DropDownMenuAuth.createDropdownMenu("Auth", mainPanel, project)
////        dropdownPanel.add(authDropdown)
////
////        // Assistant section
////        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
////        dropdownPanel.add(assistantDropdown)
////
////        // Review section
////        val reviewDropdown = DropDownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
////        dropdownPanel.add(reviewDropdown)
////
////        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
////    }
////}


package com.example.sidebarplugin

import com.example.sidebarplugin.Assistant.AssistantItems
import com.example.sidebarplugin.Documents.DocumentationItems
import com.example.sidebarplugin.GitKB.GitKBItems
import com.example.sidebarplugin.KB.KBItems
import com.example.sidebarplugin.Review.ReviewItems
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import java.awt.*
import javax.swing.*

class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
    private val mainPanel: JPanel = JPanel()

    init {
        mainPanel.layout = BorderLayout()
        addProjectNameHeader()
        addDropdownMenu()
        setContent(mainPanel)
    }

    private fun addProjectNameHeader() {
        val projectNameLabel = JLabel("Project: ${project.name}")
        projectNameLabel.font = Font("Arial", Font.BOLD, 14)
        projectNameLabel.horizontalAlignment = SwingConstants.CENTER

        mainPanel.add(projectNameLabel, BorderLayout.NORTH)
    }

    private fun addDropdownMenu() {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        // Auth dropdown
        val authDropdown = DropDownMenuAuth.createDropdownMenu("Auth", mainPanel, project)
        dropdownPanel.add(CollapsibleSection("Auth", authDropdown))

        // Assistant dropdown
        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
        dropdownPanel.add(CollapsibleSection("Assistant", assistantDropdown))

        // Review dropdown
        val reviewDropdown = DropDownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
        dropdownPanel.add(CollapsibleSection("Review", reviewDropdown))

        // Documentation dropdown
        val documentationDropdown = DropDownMenuDocumentation.createDropdownMenu("Documentation", DocumentationItems.getItems(), project)
        dropdownPanel.add(CollapsibleSection("Documentation", documentationDropdown))

        // GitKB dropdown
        val gitKBDropdown = DropdownMenuGitKB.createDropdownMenu("GitKB", GitKBItems.getItems(), project)
        dropdownPanel.add(CollapsibleSection("GitKB", gitKBDropdown))

        // KB dropdown
        val KBDropdown = DropdownMenuKB.createDropdownMenu("Knowledge Base", KBItems.getItems(), project)
        dropdownPanel.add(CollapsibleSection("Knowledge Base", KBDropdown))

        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
    }
}
