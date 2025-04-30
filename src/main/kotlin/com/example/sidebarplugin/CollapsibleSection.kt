//package com.example.sidebarplugin
//
//import java.awt.BorderLayout
//import java.awt.Component
//import javax.swing.*
//
//class CollapsibleSection(title: String, content: JComponent) : JPanel() {
//    private val contentPanel = JPanel(BorderLayout())
//
//    init {
//        layout = BoxLayout(this, BoxLayout.Y_AXIS)
//
//        val toggleButton = JButton(title)
//        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
//        var expanded = true
//
//        contentPanel.add(content, BorderLayout.CENTER)
//        contentPanel.isVisible = expanded
//
//        toggleButton.addActionListener {
//            expanded = !expanded
//            contentPanel.isVisible = expanded
//            revalidate()
//            repaint()
//        }
//
//        add(toggleButton)
//        add(contentPanel)
//    }
//}

//arrow open close symbol
package com.example.sidebarplugin

import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

class CollapsibleSection(title: String, content: JComponent) : JPanel(BorderLayout()) {
    private val toggleButton = JButton("▶ $title")
    private var isExpanded = false
    private val contentPanel = JPanel(BorderLayout())

    init {
        border = EmptyBorder(5, 5, 5, 5)
        toggleButton.horizontalAlignment = SwingConstants.LEFT
        toggleButton.isFocusPainted = false
        toggleButton.background = UIManager.getColor("Panel.background")
        toggleButton.border = BorderFactory.createEmptyBorder()
        toggleButton.addActionListener {
            toggle()
        }

        contentPanel.add(content, BorderLayout.CENTER)
        contentPanel.isVisible = false

        add(toggleButton, BorderLayout.NORTH)
        add(contentPanel, BorderLayout.CENTER)
    }

    private fun toggle() {
        isExpanded = !isExpanded
        toggleButton.text = if (isExpanded) "▼ ${toggleButton.text.substring(2)}" else "▶ ${toggleButton.text.substring(2)}"
        contentPanel.isVisible = isExpanded
        revalidate()
        repaint()
    }
}

// box and arrow open
//package com.example.sidebarplugin
//
//import java.awt.*
//import javax.swing.*
//import javax.swing.border.EmptyBorder
//
//class CollapsibleSection(private val title: String, content: JComponent) : JPanel() {
//    private val toggleLabel = JLabel()
//    private val contentPanel = JPanel(BorderLayout())
//    private var isExpanded = true
//
//    init {
//        layout = BorderLayout()
//        border = EmptyBorder(5, 5, 5, 5)
//
//        toggleLabel.text = "▼ $title"
//        toggleLabel.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
//        toggleLabel.font = Font("Arial", Font.BOLD, 13)
//        toggleLabel.border = EmptyBorder(4, 4, 4, 4)
//        toggleLabel.addMouseListener(object : java.awt.event.MouseAdapter() {
//            override fun mouseClicked(e: java.awt.event.MouseEvent?) {
//                toggleContent()
//            }
//        })
//
//        contentPanel.add(content, BorderLayout.CENTER)
//        contentPanel.isVisible = true
//
//        add(toggleLabel, BorderLayout.NORTH)
//        add(contentPanel, BorderLayout.CENTER)
//    }
//
//    private fun toggleContent() {
//        isExpanded = !isExpanded
//        toggleLabel.text = if (isExpanded) "▼ $title" else "▶ $title"
//        contentPanel.isVisible = isExpanded
//        revalidate()
//        repaint()
//    }
//}
