package com.example.sidebarplugin

import com.example.sidebarplugin.GitKB.GitKBActions
import com.example.sidebarplugin.utils.IconUtils
import com.intellij.openapi.project.Project
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*

object DropdownMenuGitKB {
    fun createDropdownMenu(title: String, items: List<String>, project: Project): JPanel {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        val iconMap = mapOf(
            "Explain" to IconUtils.load("Explain.svg"),
            "Get Code" to IconUtils.load("Get_code.svg")
        )

        items.forEach { item ->
            val label = JLabel(item, iconMap[item], JLabel.LEFT)
            label.iconTextGap = 8
            label.alignmentX = Component.LEFT_ALIGNMENT
            label.border = BorderFactory.createEmptyBorder(4, 0, 4, 4)
            label.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            label.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    GitKBActions.handleGitKBRequest(project, item)
                }
            })

            dropdownPanel.add(label)
        }

        // Wrap the dropdownPanel in a JScrollPane
        val scrollPane = JScrollPane(dropdownPanel)
        // Use default IntelliJ Look & Feel border for consistency

        // Container panel to return
        val container = JPanel(BorderLayout())
        container.add(scrollPane, BorderLayout.CENTER)

        return container
    }
}
