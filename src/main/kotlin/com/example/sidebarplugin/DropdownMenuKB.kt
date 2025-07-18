package com.example.sidebarplugin
import com.example.sidebarplugin.KB.KBActions
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import javax.swing.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

object DropdownMenuKB {
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
                    KBActions.handleKBRequest(project, selectedItem)
                    subcategoryList.clearSelection() // Ensures re-selection triggers the API call again
                }
            }
        })

        dropdownPanel.add(JScrollPane(subcategoryList))
        return dropdownPanel
    }
}