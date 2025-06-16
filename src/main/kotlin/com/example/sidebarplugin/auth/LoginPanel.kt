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
    //    private val emailField = JTextField("rahul97@gmail.com", 20).apply {
    private val emailField = JTextField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    //    private val passwordField = JPasswordField("Test@123", 20).apply {
    private val passwordField = JPasswordField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val urlState = ServiceManager.getService(PersistentState::class.java) // Global storage

    init {
        // Title Label
        val titleLabel = JLabel("Login")
        titleLabel.font = titleLabel.font.deriveFont(Font.BOLD, 18f)
        titleLabel.alignmentX = Component.CENTER_ALIGNMENT
        titleLabel.horizontalAlignment = SwingConstants.CENTER

        val titlePanel = JPanel()
        titlePanel.layout = BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        titlePanel.add(titleLabel)
        titlePanel.add(Box.createVerticalStrut(15))

        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            insets = Insets(5, 5, 5, 5) // Reduce spacing
            fill = GridBagConstraints.HORIZONTAL
        }

        // Add the title panel first
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.gridwidth = GridBagConstraints.REMAINDER
        constraints.anchor = GridBagConstraints.CENTER
        add(titlePanel, constraints)

        // Email Label
        constraints.gridx = 0
        constraints.gridy = 1
        constraints.anchor = GridBagConstraints.WEST
        add(JLabel("Email:"), constraints)

        // Email Field
        constraints.gridx = 0
        constraints.gridy = 2
        add(emailField, constraints)

        // Password Label
        constraints.gridx = 0
        constraints.gridy = 3
        add(JLabel("Password:"), constraints)

        // Password Field
        constraints.gridx = 0
        constraints.gridy = 4
        add(passwordField, constraints)

        // Submit Button
        constraints.gridx = 0
        constraints.gridy = 5
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
        constraints.gridy = 6
        val registerLabel = JLabel("Don't have an account?")
        val registerButton = JButton("Register").apply {
            preferredSize = Dimension(100, 30)
            addActionListener { replacePanelWithRegisterPanel() }
        }

        // Use BoxLayout to ensure side-by-side alignment
        val registerPanel = JPanel()
        registerPanel.layout = BoxLayout(registerPanel, BoxLayout.X_AXIS)
        registerPanel.maximumSize = Dimension(Int.MAX_VALUE, 30) // Prevent shrinking below 1 line
        registerPanel.add(registerLabel)
        registerPanel.add(Box.createHorizontalStrut(10)) // spacing between label and button
        registerPanel.add(registerButton)
        add(registerPanel, constraints)

        // Back Button
        constraints.gridy = 7
        val backButton = JButton("Back").apply {
            preferredSize = Dimension(100, 30)
            addActionListener {
                returnToMainSidePanel()
            }
        }
        val backPanel = JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            add(backButton)
        }
        add(backPanel, constraints)
    }

    // Function to display a message dialog
    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Login", messageType)
    }

    // Function to submit login form to API
    private fun submitLoginForm(email: String, password: String) {
        val persistentState = ServiceManager.getService(PersistentState::class.java)
        val storedUrl = persistentState.getStoredUrl()?.trimEnd('/') ?: ""
        SwingUtilities.invokeLater {
            try {
                val apiUrl = "$storedUrl/auth/login" // Replace with your API URL
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

//                    val errorBody = BufferedReader(InputStreamReader(connection.errorStream)).readText()
////
//                    val msg = try {
//                        val jsonError = Json.parseToJsonElement(errorBody).jsonObject
//                        println("Raw API Response (jsonerror): $jsonError")
//                        val detailArray = jsonError["detail"]?.jsonArray
//                        val firstDetail = detailArray?.getOrNull(0)?.jsonObject
//                        val message = firstDetail?.get("msg")?.jsonPrimitive?.content ?: "Unknown error"
//                        message
//                    } catch (e: Exception) {
//                        "An unexpected error occurred"
//                    }
//
//                    showMessage("Login Failed: $msg", JOptionPane.ERROR_MESSAGE)
                    val errorBody = BufferedReader(InputStreamReader(connection.errorStream)).readText()

                    val msg = try {
                        val jsonError = Json.parseToJsonElement(errorBody).jsonObject
                        println("Raw API Response (jsonerror): $jsonError")

                        val detailElement = jsonError["detail"]
                        when {
                            detailElement == null -> "Unknown error"
                            detailElement is JsonPrimitive -> detailElement.content  // directly string
                            detailElement is JsonArray -> {
                                val firstDetail = detailElement.getOrNull(0)?.jsonObject
                                firstDetail?.get("msg")?.jsonPrimitive?.content ?: "Unknown error"
                            }
                            else -> "Unknown error"
                        }
                    } catch (e: Exception) {
                        "An unexpected error occurred"
                    }

                    showMessage(msg, JOptionPane.ERROR_MESSAGE)
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

    private fun returnToMainSidePanel() {
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