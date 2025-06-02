package com.example.sidebarplugin

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import com.example.sidebarplugin.auth.UrlSubmitPanel
import com.example.sidebarplugin.auth.LoginPanel
import com.example.sidebarplugin.auth.RegisterPanel
import com.example.sidebarplugin.utils.IconUtils
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.*

object DropDownMenuAuth {
    fun createDropdownMenu(title: String, parentPanel: JPanel, project: Project): JPanel {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        val items = listOf("URL", "Login", "Register")
        val iconMap = mapOf(
            "URL" to IconUtils.load("Url.svg"),
            "Login" to IconUtils.load("Login.svg"),
            "Register" to IconUtils.load("Register.svg")
        )
        val subcategoryList = JBList(items)
        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        subcategoryList.visibleRowCount = 4

        // Custom renderer to show icons
        subcategoryList.cellRenderer = object : DefaultListCellRenderer() {
            override fun getListCellRendererComponent(
                list: JList<*>?,
                value: Any?,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                val label = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus) as JLabel
                val itemText = value as String
                label.icon = iconMap[itemText]
                label.horizontalTextPosition = JLabel.RIGHT
                label.iconTextGap = 8
                return label
            }
        }

        subcategoryList.addListSelectionListener { e ->
            if (!e.valueIsAdjusting) {
                val selectedItem = subcategoryList.selectedValue
                parentPanel.removeAll()
                parentPanel.layout = BorderLayout()
                when (selectedItem) {
                    "URL" -> parentPanel.add(UrlSubmitPanel(project), BorderLayout.CENTER)
                    "Login" -> parentPanel.add(LoginPanel(project), BorderLayout.CENTER)
                    "Register" -> parentPanel.add(RegisterPanel(project), BorderLayout.CENTER)
                }
                parentPanel.revalidate()
                parentPanel.repaint()
            }
        }

        dropdownPanel.add(JScrollPane(subcategoryList))
        return dropdownPanel
    }

}




