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
        val constraints = GridBagConstraints()
        constraints.insets = Insets(10, 10, 10, 10) // Padding for spacing

        val containerPanel = JPanel()
        containerPanel.layout = BoxLayout(containerPanel, BoxLayout.Y_AXIS)
        containerPanel.alignmentX = Component.CENTER_ALIGNMENT

        // Instruction Label
        val instructionLabel = JLabel("Please enter the domain in the below field:")
        instructionLabel.alignmentX = Component.CENTER_ALIGNMENT
        instructionLabel.horizontalAlignment = SwingConstants.CENTER

        // Panel for input field and submit button
        val inputPanel = JPanel(FlowLayout(FlowLayout.CENTER))
        inputPanel.add(JLabel("Enter URL:"))
        inputPanel.add(urlTextField)
        inputPanel.add(submitButton)

        // Add components to container panel
        containerPanel.add(instructionLabel)
        containerPanel.add(Box.createVerticalStrut(10)) // Spacing
        containerPanel.add(inputPanel)

        // Centering the container panel inside the main panel
        constraints.gridy = 0
        add(containerPanel, constraints)
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
                    showMessage("Touch API successful! Proceeding to login.", JOptionPane.INFORMATION_MESSAGE)
                    urlState.setStoredUrl(url) // Store the URL
                    println("Stored URL in UrlState: ${urlState.getStoredUrl()}") // Print the stored URL to the console
                    replacePanelWithLoginForm(url)
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

    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Touch API Result", messageType)
    }
}