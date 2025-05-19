// In a file like RepoDocumentationPanel.kt
package com.example.sidebarplugin.ui

import java.awt.*
import javax.swing.*
import javax.swing.border.EmptyBorder

fun createRepoDocumentationUI(onAccept: () -> Unit, onReject: () -> Unit): JPanel {
    val mainPanel = JPanel()
    mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
    mainPanel.border = EmptyBorder(20, 20, 20, 20)

    val titleLabel = JLabel("📘 Repository Documentation")
    titleLabel.font = titleLabel.font.deriveFont(20f)
    titleLabel.alignmentX = Component.CENTER_ALIGNMENT
    mainPanel.add(titleLabel)

    mainPanel.add(Box.createVerticalStrut(15))

    val subtitle = JLabel("Generate documentation by providing the following details:")
    subtitle.alignmentX = Component.CENTER_ALIGNMENT
    mainPanel.add(subtitle)
    mainPanel.add(Box.createVerticalStrut(30))

    val formPanel = JPanel()
    formPanel.layout = BoxLayout(formPanel, BoxLayout.Y_AXIS)
    formPanel.alignmentX = Component.CENTER_ALIGNMENT // ⬅ Center the form on the main panel

    fun createLabeledField(labelText: String): JTextField {
        val label = JLabel(labelText)
        label.alignmentX = Component.CENTER_ALIGNMENT

        val textField = JTextField(30)
        textField.maximumSize = Dimension(300, 24) // Uniform width and shorter height
        textField.alignmentX = Component.CENTER_ALIGNMENT

        val fieldPanel = JPanel()
        fieldPanel.layout = BoxLayout(fieldPanel, BoxLayout.Y_AXIS)
        fieldPanel.border = EmptyBorder(15, 0, 10, 0) // Bottom spacing between fields
        fieldPanel.alignmentX = Component.CENTER_ALIGNMENT

        fieldPanel.add(label)
        fieldPanel.add(Box.createVerticalStrut(4))
        fieldPanel.add(textField)

        formPanel.add(fieldPanel)

        return textField
    }

    val repoField = createLabeledField("Repository URL *")
    val patField = createLabeledField("Personal Access Token (PAT)")
    val branchField = createLabeledField("Branch Name *")

    mainPanel.add(formPanel)


    val statusLabel = JLabel(" ")
    statusLabel.foreground = Color.RED
    statusLabel.alignmentX = Component.CENTER_ALIGNMENT  // Center horizontally

    mainPanel.add(Box.createVerticalStrut(10))
    mainPanel.add(statusLabel)
    mainPanel.add(Box.createVerticalStrut(10))


    val markdownArea = JTextArea(10, 80)
    markdownArea.isEditable = false
    markdownArea.lineWrap = true
    markdownArea.wrapStyleWord = true
    val scrollPane = JScrollPane(markdownArea)
    scrollPane.isVisible = false
    mainPanel.add(scrollPane)

    val buttonPanel = JPanel(FlowLayout(FlowLayout.CENTER))
    val submitButton = JButton("Submit")
    val cancelButton = JButton("Cancel")
    buttonPanel.add(submitButton)
    buttonPanel.add(cancelButton)
    mainPanel.add(buttonPanel)

    submitButton.addActionListener {
        val repo = repoField.text.trim()
        val pat = patField.text.trim()
        val branch = branchField.text.trim()

        if (repo.isEmpty() || branch.isEmpty()) {
            statusLabel.text = "Please fill in all required fields."
            return@addActionListener
        }

        statusLabel.text = "Submitting..."
        scrollPane.isVisible = false

        SwingUtilities.invokeLater {
            statusLabel.text = "Documentation generated successfully."
            markdownArea.text = "## Sample Repository Documentation\n\nThis is where your generated markdown would be displayed."
            scrollPane.isVisible = true
            mainPanel.revalidate()
        }
    }

    cancelButton.addActionListener {
        statusLabel.text = "Cancelled."
        scrollPane.isVisible = false
        onReject()
    }

    return mainPanel
}
