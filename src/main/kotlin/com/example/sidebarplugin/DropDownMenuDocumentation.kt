package com.example.sidebarplugin

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import com.example.sidebarplugin.Documents.DocumentationResponse.DocumentationActions


class DropDownMenuDocumentation {
    companion object {
        fun createDropdownMenu(title: String, items: List<String>, project: Project): JPanel {
            val dropdownPanel = JPanel()
            dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

            val subcategoryList = JBList(items)
            subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
            subcategoryList.visibleRowCount = 4

            subcategoryList.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    val selectedItem = subcategoryList.selectedValue
                    if (selectedItem != null) {
                        DocumentationActions.handleDocumentationRequest(project, selectedItem)
                        subcategoryList.clearSelection() // Allows re-selection to re-trigger
                    }
                }
            })


            dropdownPanel.add(JScrollPane(subcategoryList))
            return dropdownPanel
        }
    }
}
