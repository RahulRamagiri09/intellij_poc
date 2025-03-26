package com.example.sidebarplugin

import com.example.sidebarplugin.Assistant.AssistantItems
import com.example.sidebarplugin.Review.ReviewItems
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

        // Usage
        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
        dropdownPanel.add(assistantDropdown)

        val reviewDropdown = DropDownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
        dropdownPanel.add(reviewDropdown)

        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
    }
}
