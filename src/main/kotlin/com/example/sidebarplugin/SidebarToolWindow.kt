//package com.example.sidebarplugin
//
//import com.example.sidebarplugin.Assistant.AssistantItems
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
//    private fun addDropdownMenu() {
//        val categories = listOf("Assistant", "Review")
//        val categoryList = JBList(categories)
//        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Usage
//        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
//        dropdownPanel.add(assistantDropdown)
//
//        val reviewDropdown = DropdownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
//        dropdownPanel.add(reviewDropdown)
//
//        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
//    }
//}
//

package com.example.sidebarplugin

import com.example.sidebarplugin.Assistant.AssistantItems
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import javax.swing.*
import java.awt.*

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
        val categories = listOf("Assistant", "Review")
        val categoryList = JBList(categories)
        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        // Auth section
        val authDropdown = DropDownMenuAuth.createDropdownMenu("Auth", mainPanel, project)

        dropdownPanel.add(authDropdown)

        // Assistant section
        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
        dropdownPanel.add(assistantDropdown)

        // Review section
        val reviewDropdown = DropdownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
        dropdownPanel.add(reviewDropdown)

        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
    }

    private fun createDropdownMenu(title: String, items: List<String>): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        val label = JLabel(title)
        label.font = Font("Arial", Font.BOLD, 12)
        panel.add(label)

        val list = JBList(items)
        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        panel.add(JBScrollPane(list))

        return panel
    }
}
