//package com.example.sidebarplugin
//import java.awt.*
//import javax.swing.*
//import java.net.HttpURLConnection
//import java.net.URL
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import com.intellij.openapi.project.Project
//import kotlinx.serialization.json.*
//
//class UrlSubmitPanel(private val project: Project) : JPanel() {
//    private val urlTextField = JTextField(20) // Text field for URL input
//    private val submitButton = JButton("Submit") // Submit button
//    init {
//        layout = BorderLayout()
//        val instructionLabel = JLabel("Please enter the domain in the below field:")
//        instructionLabel.horizontalAlignment = SwingConstants.CENTER
//        add(instructionLabel, BorderLayout.NORTH)
//        // Panel for input field and submit button
//        val inputPanel = JPanel()
//        inputPanel.layout = FlowLayout(FlowLayout.CENTER)
//        // Add label, text field, and button to the panel
//        inputPanel.add(JLabel("Enter URL:"))
//        inputPanel.add(urlTextField)
//        inputPanel.add(submitButton)
//        // Add the input panel to the center of the UrlSubmitPanel
//        add(inputPanel, BorderLayout.CENTER)
//        // Add action listener to handle the submission
//        submitButton.addActionListener {
//            val url = urlTextField.text.trim()
//            if (url.isEmpty()) {
//                showMessage("Please enter a URL.", JOptionPane.WARNING_MESSAGE)
//            } else {
//                // Directly append '/touch' to the entered URL
//                val finalUrl = "$url/touch" // Append '/touch'
//                println("Final URL to hit: $finalUrl") // Log for debugging
//                submitTouchRequest(finalUrl)
//            }
//        }
//    }
//    // Function to submit the touch request to the entered URL
//    private fun submitTouchRequest(url: String) {
//        SwingUtilities.invokeLater {
//            try {
//                val connection = URL(url).openConnection() as HttpURLConnection
//                connection.requestMethod = "GET" // Use GET method for the touch endpoint
//                val responseCode = connection.responseCode
//                println("Response Code: $responseCode") // Print the response code for debugging
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//                    // On success, replace this panel with LoginPanel
//                    showMessage("Touch API successful! Proceeding to login.", JOptionPane.INFORMATION_MESSAGE)
//                    replacePanelWithLoginForm()
//                } else {
//                    val errorMessage = BufferedReader(InputStreamReader(connection.errorStream)).readText()
//                    showMessage("Touch API failed: $errorMessage", JOptionPane.ERROR_MESSAGE)
//                }
//                connection.disconnect()
//            } catch (e: Exception) {
//                showMessage("An error occurred: ${e.message}", JOptionPane.ERROR_MESSAGE)
//                e.printStackTrace() // Print stack trace for debugging
//            }
//        }
//    }
//    // Function to replace UrlSubmitPanel with LoginPanel
//    private fun replacePanelWithLoginForm() {
//        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
//        if (parentToolWindow is JPanel) {
//            parentToolWindow.removeAll()
//            parentToolWindow.layout = BorderLayout()
//            parentToolWindow.add(LoginPanel(project), BorderLayout.CENTER) // Add LoginPanel
//            parentToolWindow.revalidate()
//            parentToolWindow.repaint()
//        }
//    }
//    // Function to display a message dialog
//    private fun showMessage(message: String, messageType: Int) {
//        JOptionPane.showMessageDialog(this, message, "Touch API Result", messageType)
//    }
//}

package com.example.sidebarplugin

import java.awt.*
import javax.swing.*
import java.net.HttpURLConnection
import java.net.URL
import java.io.BufferedReader
import java.io.InputStreamReader
import com.intellij.openapi.project.Project
import kotlinx.serialization.json.*

class UrlSubmitPanel(private val project: Project) : JPanel() {
    private val urlTextField = JTextField(20) // Text field for URL input
    private val submitButton = JButton("Submit") // Submit button

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
                val finalUrl = "$url/touch"
                println("Final URL to hit: $finalUrl")
                submitTouchRequest(finalUrl)
            }
        }
    }

    private fun submitTouchRequest(url: String) {
        SwingUtilities.invokeLater {
            try {
                val connection = URL(url).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val responseCode = connection.responseCode
                println("Response Code: $responseCode")
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showMessage("Touch API successful! Proceeding to login.", JOptionPane.INFORMATION_MESSAGE)
                    replacePanelWithLoginForm()
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

    private fun replacePanelWithLoginForm() {
        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
        if (parentToolWindow is JPanel) {
            parentToolWindow.removeAll()
            parentToolWindow.layout = BorderLayout()
            parentToolWindow.add(LoginPanel(project), BorderLayout.CENTER)
            parentToolWindow.revalidate()
            parentToolWindow.repaint()
        }
    }

    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Touch API Result", messageType)
    }
}
