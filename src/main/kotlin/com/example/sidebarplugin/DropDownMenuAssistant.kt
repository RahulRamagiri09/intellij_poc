package com.example.sidebarplugin

import com.example.sidebarplugin.Assistant.AssistantActions
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import javax.swing.*
import java.awt.Component
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

object DropDownMenuAssistant {
    fun createDropdownMenu(title: String, items: List<String>, project: Project): JPanel {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        val toggleButton = JButton(title)
        toggleButton.alignmentX = Component.LEFT_ALIGNMENT

        val subcategoryList = JBList(items)
        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        subcategoryList.visibleRowCount = 4

        subcategoryList.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val selectedItem = subcategoryList.selectedValue
                if (selectedItem != null) {
                    AssistantActions.handleAssistantRequest(project, selectedItem)
                    subcategoryList.clearSelection() // Ensures re-selection triggers the API call again
                }
            }
        })

        dropdownPanel.add(toggleButton)
        dropdownPanel.add(JScrollPane(subcategoryList))

        return dropdownPanel
    }
}
