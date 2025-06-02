package com.example.sidebarplugin

import com.example.sidebarplugin.Assistant.AssistantActions
import com.example.sidebarplugin.Assistant.AssistantItem
import com.example.sidebarplugin.utils.IconUtils
import com.intellij.openapi.project.Project
import javax.swing.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

object DropDownMenuAssistant {
    fun createDropdownMenu(title: String, items: List<AssistantItem>, project: Project): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)

        val listModel = DefaultListModel<AssistantItem>()
        items.forEach { listModel.addElement(it) }

        val list = JList(listModel)
        list.cellRenderer = object : ListCellRenderer<AssistantItem> {
            override fun getListCellRendererComponent(
                list: JList<out AssistantItem>,
                value: AssistantItem,
                index: Int,
                isSelected: Boolean,
                cellHasFocus: Boolean
            ): Component {
                val label = JLabel(value.label, IconUtils.load(value.iconName), JLabel.LEFT)
                label.iconTextGap = 8
                if (isSelected) {
                    label.background = list.selectionBackground
                    label.foreground = list.selectionForeground
                    label.isOpaque = true
                }
                return label
            }
        }

        list.selectionMode = ListSelectionModel.SINGLE_SELECTION
        list.addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent?) {
                val selectedItem = list.selectedValue
                if (selectedItem != null) {
                    AssistantActions.handleAssistantRequest(project, selectedItem.label)
                    list.clearSelection()
                }
            }
        })

        panel.add(JScrollPane(list))
        return panel
    }
}
