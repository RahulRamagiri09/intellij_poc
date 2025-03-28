//package com.example.sidebarplugin
//
//import com.intellij.ui.components.JBList
//import javax.swing.*
//import java.awt.Component
//
//object DropDownMenuAuth {
//    fun createDropdownMenu(title: String): JPanel {
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        val toggleButton = JButton(title)
//        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
//
//        val items = listOf("URL", "Login", "Register")
//        val subcategoryList = JBList(items)
//        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//        subcategoryList.visibleRowCount = 4
//
//        dropdownPanel.add(toggleButton)
//        dropdownPanel.add(JScrollPane(subcategoryList))
//
//        return dropdownPanel
//    }
//}


package com.example.sidebarplugin

import com.intellij.ui.components.JBList
import javax.swing.*
import java.awt.Component
import com.intellij.openapi.project.Project
import com.example.sidebarplugin.auth.UrlSubmitPanel
import com.example.sidebarplugin.auth.LoginPanel
import com.example.sidebarplugin.auth.RegisterPanel
import java.awt.BorderLayout

object DropDownMenuAuth {
    fun createDropdownMenu(title: String, parentPanel: JPanel, project: Project): JPanel {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        val toggleButton = JButton(title)
        toggleButton.alignmentX = Component.LEFT_ALIGNMENT

        val items = listOf("URL", "Login", "Register")
        val subcategoryList = JBList(items)
        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
        subcategoryList.visibleRowCount = 4

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

        dropdownPanel.add(toggleButton)
        dropdownPanel.add(JScrollPane(subcategoryList))

        return dropdownPanel
    }
}