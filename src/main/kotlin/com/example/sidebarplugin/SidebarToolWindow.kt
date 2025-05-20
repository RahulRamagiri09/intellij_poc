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
    private val mainPanel: JPanel = JPanel(BorderLayout())

    init {
        mainPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 0) // remove any outer margin
        addProjectNameHeader()
        addDropdownMenu()
        setContent(mainPanel)
    }

    private fun addProjectNameHeader() {
        val projectNameLabel = JLabel("Project: ${project.name}")
        projectNameLabel.font = Font("Arial", Font.BOLD, 14)
        projectNameLabel.horizontalAlignment = SwingConstants.CENTER
        projectNameLabel.border = BorderFactory.createEmptyBorder(4, 0, 4, 0) // Optional minimal spacing

        mainPanel.add(projectNameLabel, BorderLayout.NORTH)
    }

    private fun addDropdownMenu() {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
        dropdownPanel.alignmentX = Component.LEFT_ALIGNMENT
        dropdownPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 0) // remove inner border

        // Add collapsible sections
        dropdownPanel.add(CollapsibleSection("Auth", DropDownMenuAuth.createDropdownMenu("Auth", mainPanel, project)))
        dropdownPanel.add(CollapsibleSection("Assistant", DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)))
        dropdownPanel.add(CollapsibleSection("Review", DropDownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)))
        dropdownPanel.add(CollapsibleSection("Documentation", DropDownMenuDocumentation.createDropdownMenu("Documentation", DocumentationItems.getItems(), project)))
        dropdownPanel.add(CollapsibleSection("GitKB", DropdownMenuGitKB.createDropdownMenu("GitKB", GitKBItems.getItems(), project)))
        dropdownPanel.add(CollapsibleSection("Knowledge Base", DropdownMenuKB.createDropdownMenu("Knowledge Base", KBItems.getItems(), project)))

        val scrollPane = JBScrollPane(dropdownPanel)
        scrollPane.border = BorderFactory.createEmptyBorder(0, 0, 0, 0) // remove border from scroll pane
        mainPanel.add(scrollPane, BorderLayout.CENTER)
    }
}
