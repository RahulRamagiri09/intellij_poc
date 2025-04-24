package com.example.sidebarplugin.auth

import com.example.sidebarplugin.SidebarToolWindow
import com.example.sidebarplugin.storage.PersistentState
import java.awt.*
import javax.swing.*
import java.io.InputStreamReader
import java.io.BufferedReader
import com.intellij.openapi.project.Project
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.*
import java.util.Base64
import javax.swing.JOptionPane
import com.intellij.openapi.components.ServiceManager

class LoginPanel(private val project: Project) : JPanel() {
    private val emailField = JTextField("rahul97@gmail.com", 20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val passwordField = JPasswordField("Test@123", 20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val urlState = ServiceManager.getService(PersistentState::class.java) // Global storage

    init {
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            insets = Insets(5, 5, 5, 5) // Reduce spacing
            fill = GridBagConstraints.HORIZONTAL
        }

        // Email Label
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.anchor = GridBagConstraints.WEST
        add(JLabel("Email:"), constraints)

        // Email Field
        constraints.gridx = 0
        constraints.gridy = 1
        add(emailField, constraints)

        // Password Label
        constraints.gridx = 0
        constraints.gridy = 2
        add(JLabel("Password:"), constraints)

        // Password Field
        constraints.gridx = 0
        constraints.gridy = 3
        add(passwordField, constraints)

        // Submit Button
        constraints.gridx = 0
        constraints.gridy = 4
        constraints.gridwidth = 1
        constraints.anchor = GridBagConstraints.CENTER
        val submitButton = JButton("Submit").apply {
            preferredSize = Dimension(100, 30)
            addActionListener {
                val email = emailField.text.trim()
                val password = String(passwordField.password).trim()
                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Email and Password cannot be empty.", JOptionPane.WARNING_MESSAGE)
                } else {
                    submitLoginForm(email, password)
                }
            }
        }
        add(submitButton, constraints)

        // Register Section
        constraints.gridy = 5
        val registerPanel = JPanel().apply {
            layout = FlowLayout(FlowLayout.CENTER, 5, 0) // Keep label and button in same line
            add(JLabel("Don't have an account?"))
            val registerButton = JButton("Register").apply {
                preferredSize = Dimension(100, 30) // Reduce button width
                addActionListener {
                    replacePanelWithRegisterPanel()
                }
            }
            add(registerButton)
        }
        add(registerPanel, constraints)
    }

    // Function to display a message dialog
    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Login", messageType)
    }

    // Function to submit login form to API
    private fun submitLoginForm(email: String, password: String) {
        SwingUtilities.invokeLater {
            try {
                val apiUrl = "http://34.46.36.105:3000/genieapi/auth/login" // Replace with your API URL
                val requestBody = "email=$email&password=$password"
                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                connection.outputStream.use { it.write(requestBody.toByteArray()) }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseBody = BufferedReader(InputStreamReader(connection.inputStream)).readText()
                    val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                    val accessToken = jsonResponse["access_token"]?.jsonPrimitive?.content

                    if (accessToken != null) {
                        urlState.setAuthToken(accessToken) // Store the auth token
                        showMessage("Login Successful!", JOptionPane.INFORMATION_MESSAGE)
                        replacePanelWithSidebarToolWindow()
                    } else {
                        showMessage("Error: No access token found", JOptionPane.ERROR_MESSAGE)
                    }
                } else {
                    val errorMessage = BufferedReader(InputStreamReader(connection.errorStream)).readText()
                    showMessage("Login Failed: $errorMessage", JOptionPane.ERROR_MESSAGE)
                }
                connection.disconnect()
            } catch (e: Exception) {
                showMessage("An error occurred: ${e.message}", JOptionPane.ERROR_MESSAGE)
            }
        }
    }

    // Function to replace LoginPanel with RegisterPanel
    private fun replacePanelWithRegisterPanel() {
        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
        if (parentToolWindow is JPanel) {
            parentToolWindow.removeAll()
            parentToolWindow.layout = BorderLayout()
            parentToolWindow.add(RegisterPanel(project), BorderLayout.CENTER)
            parentToolWindow.revalidate()
            parentToolWindow.repaint()
        }
    }

    // Function to replace LoginPanel with SidebarToolWindow after login success
    private fun replacePanelWithSidebarToolWindow() {
        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
        if (parentToolWindow is JPanel) {
            parentToolWindow.removeAll()
            parentToolWindow.layout = BorderLayout()
            parentToolWindow.add(SidebarToolWindow(project), BorderLayout.CENTER)
            parentToolWindow.revalidate()
            parentToolWindow.repaint()
        }
    }
}
