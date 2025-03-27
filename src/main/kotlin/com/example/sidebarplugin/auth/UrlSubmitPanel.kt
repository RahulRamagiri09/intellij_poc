package com.example.sidebarplugin.auth

import com.example.sidebarplugin.storage.PersistentState
import java.awt.*
import javax.swing.*
import java.net.HttpURLConnection
import java.net.URL
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.ServiceManager
import java.io.BufferedReader
import java.io.InputStreamReader

class UrlSubmitPanel(private val project: Project) : JPanel() {
    private val urlTextField = JTextField(20) // Text field for URL input
    private val submitButton = JButton("Submit") // Submit button
    private val urlState = ServiceManager.getService(PersistentState::class.java) // Global storage

    init {
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            insets = Insets(10, 10, 10, 10) // Padding for spacing
            fill = GridBagConstraints.HORIZONTAL
            anchor = GridBagConstraints.CENTER
        }

        // Main Container Panel (Vertically Aligned)
        val containerPanel = JPanel()
        containerPanel.layout = BoxLayout(containerPanel, BoxLayout.Y_AXIS)
        containerPanel.alignmentX = Component.CENTER_ALIGNMENT

        // Instruction Label
        val instructionLabel = JLabel("Please enter the domain URL in the below field:")
        instructionLabel.alignmentX = Component.CENTER_ALIGNMENT
        instructionLabel.horizontalAlignment = SwingConstants.CENTER

        // Input Field Panel (URL Label & Text Field)
        val inputPanel = JPanel()
        inputPanel.layout = BoxLayout(inputPanel, BoxLayout.Y_AXIS) // Stack components vertically
        inputPanel.alignmentX = Component.CENTER_ALIGNMENT

        urlTextField.maximumSize = Dimension(300, 30) // Fixed width for the text field
        urlTextField.alignmentX = Component.CENTER_ALIGNMENT

        // Submit Button Panel (to center align)
        val buttonPanel = JPanel()
        buttonPanel.layout = BoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        submitButton.alignmentX = Component.CENTER_ALIGNMENT

        // Add components to the panels

        inputPanel.add(Box.createVerticalStrut(5)) // Space between label and text field
        inputPanel.add(urlTextField)

        buttonPanel.add(Box.createVerticalStrut(5)) // Space between input and button
        buttonPanel.add(submitButton)

        // Add components to the main container panel
        containerPanel.add(instructionLabel)
        containerPanel.add(Box.createVerticalStrut(10)) // Space
        containerPanel.add(inputPanel)
        containerPanel.add(buttonPanel)

        // Add the container panel to the main layout
        constraints.gridy = 0
        add(containerPanel, constraints)

        // Default URL Value
        urlTextField.text = "http://34.46.36.105:3000/genieapi"

        // Button Action Listener
        submitButton.addActionListener {
            val url = urlTextField.text.trim()
            if (url.isEmpty()) {
                showMessage("Please enter a URL.", JOptionPane.WARNING_MESSAGE)
            } else {
                val touchUrl = "$url/touch"
                println("Final URL to hit: $touchUrl")
                submitTouchRequest(touchUrl, url)
            }
        }
    }

    private fun submitTouchRequest(touchUrl: String, url: String) {
        SwingUtilities.invokeLater {
            try {
                val connection = URL(touchUrl).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val responseCode = connection.responseCode
                println("Response Code: $responseCode")
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showCustomMessage("Url submitted successfully! Proceed to Login/Register....", url)
                    urlState.setStoredUrl(url) // Store the URL
                    println("Stored URL in UrlState: ${urlState.getStoredUrl()}") // Print stored URL
                } else {
                    val errorMessage = BufferedReader(InputStreamReader(connection.errorStream)).readText()
                    showMessage("Touch API failed: $errorMessage", JOptionPane.ERROR_MESSAGE)
                }
                connection.disconnect()
            } catch (e: Exception) {
                showMessage("An error occurred: ${e.message}", JOptionPane.ERROR_MESSAGE)
                e.printStackTrace()
            }
        }
    }

    private fun showCustomMessage(message: String, url: String) {
        val dialog = JDialog(SwingUtilities.getWindowAncestor(this), "Submit URL", Dialog.ModalityType.APPLICATION_MODAL)
        dialog.layout = BorderLayout()

        // Create a panel with padding
        val messagePanel = JPanel()
        messagePanel.layout = BorderLayout()
        messagePanel.border = BorderFactory.createEmptyBorder(20, 30, 20, 30) // Add padding (top, left, bottom, right)

        val messageLabel = JLabel(message)
        messageLabel.horizontalAlignment = SwingConstants.CENTER

        messagePanel.add(messageLabel, BorderLayout.CENTER)

        dialog.add(messagePanel, BorderLayout.CENTER)

//        val messageLabel = JLabel(message)
//        messageLabel.horizontalAlignment = SwingConstants.CENTER
//        dialog.add(messageLabel, BorderLayout.CENTER)

        val buttonPanel = JPanel()
        buttonPanel.layout = FlowLayout(FlowLayout.CENTER)

        val loginButton = JButton("Login")
        val registerButton = JButton("Register")

        loginButton.addActionListener {
            dialog.dispose()
            replacePanelWithLoginForm(url)
        }

        registerButton.addActionListener {
            dialog.dispose()
            replacePanelWithRegisterForm(url)
        }

        buttonPanel.add(loginButton)
        buttonPanel.add(registerButton)

        dialog.add(buttonPanel, BorderLayout.SOUTH)
        dialog.pack()
        dialog.setLocationRelativeTo(this)
        dialog.isVisible = true
    }

    private fun replacePanelWithLoginForm(url: String) {
        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
        if (parentToolWindow is JPanel) {
            parentToolWindow.removeAll()
            parentToolWindow.layout = BorderLayout()
            parentToolWindow.add(LoginPanel(project), BorderLayout.CENTER)
            parentToolWindow.revalidate()
            parentToolWindow.repaint()
        }
        println("Navigating to login page with URL: $url")
    }

    private fun replacePanelWithRegisterForm(url: String) {
        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
        if (parentToolWindow is JPanel) {
            parentToolWindow.removeAll()
            parentToolWindow.layout = BorderLayout()
            parentToolWindow.add(RegisterPanel(project), BorderLayout.CENTER)
            parentToolWindow.revalidate()
            parentToolWindow.repaint()
        }
        println("Navigating to register page with URL: $url")
    }

    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Touch API Result", messageType)
    }
}
