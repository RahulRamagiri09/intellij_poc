package com.example.sidebarplugin


import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import javax.swing.*
import java.awt.Component
import com.example.sidebarplugin.AssistantActions

object DropDownMenuAssistant {
    fun createDropdownMenu(title: String, items: List<String>, project: Project): JPanel {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        val toggleButton = JButton(title)
        toggleButton.alignmentX = Component.LEFT_ALIGNMENT

        val subcategoryList = JBList(items)
        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        subcategoryList.visibleRowCount = 4

        subcategoryList.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                val selectedItem = subcategoryList.selectedValue
                if (selectedItem != null) {
                    // Call handleAssistantRequest and get the response
                    val response = AssistantActions.handleAssistantRequest(project, selectedItem)

                    // Show API response in a JOptionPane
//                    JOptionPane.showMessageDialog(
//                        null,
//                        response,
//                        "$title - Assistant Result",
//                        JOptionPane.INFORMATION_MESSAGE
//                    )
                }
            }
        }

        dropdownPanel.add(toggleButton)
        dropdownPanel.add(JScrollPane(subcategoryList))

        return dropdownPanel
    }
}
