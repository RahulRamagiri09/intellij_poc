package com.example.sidebarplugin

import java.awt.Component
import java.awt.Dimension
import javax.swing.*
import javax.swing.border.EmptyBorder

class CollapsibleSection(title: String, content: JComponent) : JPanel() {
    private val toggleButton = JButton("▶ $title")
    private var isExpanded = false
    private val contentPanel = JPanel()

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        alignmentX = Component.LEFT_ALIGNMENT
        border = BorderFactory.createEmptyBorder() // Remove any outer border

        // Toggle button setup (no border, no padding)
        toggleButton.horizontalAlignment = SwingConstants.LEFT
        toggleButton.isFocusPainted = false
        toggleButton.isContentAreaFilled = false
        toggleButton.border = BorderFactory.createEmptyBorder() // Remove button border
        toggleButton.background = UIManager.getColor("Panel.background")
        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
        toggleButton.maximumSize = Dimension(Int.MAX_VALUE, toggleButton.preferredSize.height)
        toggleButton.addActionListener { toggle() }

        // Content panel setup with no border
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
        contentPanel.add(content)
        contentPanel.border = BorderFactory.createEmptyBorder() // Ensure no inner border
        contentPanel.alignmentX = Component.LEFT_ALIGNMENT
        contentPanel.isVisible = false
        contentPanel.maximumSize = Dimension(0, 0) // No size when collapsed

        add(toggleButton)
        add(contentPanel)
    }

    private fun toggle() {
        isExpanded = !isExpanded
        toggleButton.text = if (isExpanded) "▼ ${toggleButton.text.substring(2)}" else "▶ ${toggleButton.text.substring(2)}"

        if (isExpanded) {
            contentPanel.isVisible = true
            contentPanel.maximumSize = Dimension(Int.MAX_VALUE, Int.MAX_VALUE)
        } else {
            contentPanel.isVisible = false
            contentPanel.maximumSize = Dimension(0, 0)
        }

        revalidate()
        repaint()
    }
}
