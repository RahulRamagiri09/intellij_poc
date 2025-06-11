package com.example.sidebarplugin

import com.intellij.openapi.project.Project
import com.example.sidebarplugin.Documents.DocumentationActions
import com.example.sidebarplugin.utils.IconUtils
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

class DropDownMenuDocumentation {
    companion object {
        fun createDropdownMenu(title: String, items: List<String>, project: Project): JPanel {
            val dropdownPanel = JPanel()
            dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

            val icon = IconUtils.load("Documentation.svg")

            items.forEach { item ->
                val label = JLabel(item, icon, JLabel.LEFT)
                label.iconTextGap = 8
                label.alignmentX = Component.LEFT_ALIGNMENT
                label.border = BorderFactory.createEmptyBorder(4, 0, 4, 4)
                label.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

                label.addMouseListener(object : MouseAdapter() {
                    override fun mouseClicked(e: MouseEvent?) {
                        DocumentationActions.handleDocumentationRequest(project, item)
                    }
                })

                dropdownPanel.add(label)
            }

            // Wrap dropdownPanel in a JScrollPane
            val scrollPane = JScrollPane(dropdownPanel)
            // Use default IntelliJ Look & Feel border (no custom border set)

            // Container panel to return
            val container = JPanel(BorderLayout())
            container.add(scrollPane, BorderLayout.CENTER)

            return container
        }
    }
}
