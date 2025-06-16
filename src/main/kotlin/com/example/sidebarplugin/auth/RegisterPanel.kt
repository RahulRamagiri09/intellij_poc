package com.example.sidebarplugin.auth

import com.example.sidebarplugin.storage.PersistentState
import java.awt.*
import javax.swing.*
import java.io.InputStreamReader
import java.io.BufferedReader
import com.intellij.openapi.project.Project
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.json.*
import javax.swing.JOptionPane
import com.intellij.openapi.components.ServiceManager
import kotlinx.serialization.encodeToString
import com.example.sidebarplugin.SidebarToolWindow

class RegisterPanel(private val project: Project) : JPanel() {
    private val usernameField = JTextField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val fullNameField = JTextField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
//    private val emailField = JTextField("rahul97@gmail.com", 20).apply {
private val emailField = JTextField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val passwordField = JPasswordField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val confirmPasswordField = JPasswordField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val companyNameField = JTextField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val urlState = ServiceManager.getService(PersistentState::class.java)

    init {
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            insets = Insets(5, 5, 5, 5)
            fill = GridBagConstraints.HORIZONTAL
        }

        // Title Label
        val titleLabel = JLabel("Register").apply {
            font = Font("Arial", Font.BOLD, 20)
            horizontalAlignment = SwingConstants.CENTER
        }
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.gridwidth = 2
        add(titleLabel, constraints)
        constraints.gridwidth = 1  // Reset gridwidth after title

        // Form fields
        addField("Username:", usernameField, constraints, 1)
        addField("Full Name:", fullNameField, constraints, 2)
        addField("Email:", emailField, constraints, 3)
        addField("Password:", passwordField, constraints, 4)
        addField("Confirm Password:", confirmPasswordField, constraints, 5)
        addField("Company Name:", companyNameField, constraints, 6)

        // Register Button
        constraints.gridy = 7
        val registerButton = JButton("Submit").apply {
            preferredSize = Dimension(100, 30)
            addActionListener {
                val username = usernameField.text.trim()
                val fullName = fullNameField.text.trim()
                val email = emailField.text.trim()
                val password = String(passwordField.password).trim()
                val confirmPassword = String(confirmPasswordField.password).trim()
                val companyName = companyNameField.text.trim()

                if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || companyName.isEmpty()) {
                    showMessage("All fields are required.", JOptionPane.WARNING_MESSAGE)
                } else if (!email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"))) {
                    showMessage("Please enter a valid email address.", JOptionPane.ERROR_MESSAGE)
                }
                else if (password != confirmPassword) {
                    showMessage("Passwords do not match.", JOptionPane.ERROR_MESSAGE)
                } else {
                    submitRegisterForm(username, fullName, email, password, confirmPassword, companyName)
                }
            }
        }
        constraints.gridx = 0
        constraints.gridwidth = 2
        add(registerButton, constraints)

        // Login Section
        constraints.gridy = 8
        val loginLabel = JLabel("Already have an account?")
        val loginButton = JButton("Login").apply {
            preferredSize = Dimension(100, 30)
            maximumSize = Dimension(100, 30)
            addActionListener {
                replacePanelWithLoginPanel()
            }
        }

        val loginPanel = JPanel()
        loginPanel.layout = BoxLayout(loginPanel, BoxLayout.X_AXIS)
        loginPanel.maximumSize = Dimension(Int.MAX_VALUE, 30)
        loginPanel.add(loginLabel)
        loginPanel.add(Box.createHorizontalStrut(10)) // spacing
        loginPanel.add(loginButton)
        add(loginPanel, constraints)

        // Back Button Section
        constraints.gridy = 9
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

    private fun addField(label: String, field: JComponent, constraints: GridBagConstraints, row: Int) {
        constraints.gridx = 0
        constraints.gridy = row
        add(JLabel(label), constraints)

        constraints.gridx = 1
        add(field, constraints)
    }

    private fun submitRegisterForm(username: String, fullName: String, email: String, password: String, confirmPassword: String, companyName: String) {
        val persistentState = ServiceManager.getService(PersistentState::class.java)
        val storedUrl = persistentState.getStoredUrl()?.trimEnd('/') ?: ""
        SwingUtilities.invokeLater {
            try {
                val apiUrl = "$storedUrl/auth/register"
                val requestBody = Json.encodeToString(JsonObject(mapOf(
                    "username" to JsonPrimitive(username),
                    "full_name" to JsonPrimitive(fullName),
                    "email" to JsonPrimitive(email),
                    "password" to JsonPrimitive(password),
                    "confirm_password" to JsonPrimitive(confirmPassword),
                    "company_name" to JsonPrimitive(companyName)
                )))

                val url = URL(apiUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.doOutput = true
                connection.setRequestProperty("Content-Type", "application/json")
                connection.outputStream.use { it.write(requestBody.toByteArray()) }

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    showMessage("Registration Successful! Please proceed to login.", JOptionPane.INFORMATION_MESSAGE)
                    replacePanelWithLoginPanel()
                } else {
                    val errorMessage = BufferedReader(InputStreamReader(connection.errorStream)).readText()
                    showMessage("Registration Failed: $errorMessage", JOptionPane.ERROR_MESSAGE)
                }
                connection.disconnect()
            } catch (e: Exception) {
                showMessage("An error occurred: ${e.message}", JOptionPane.ERROR_MESSAGE)
            }
        }
    }

    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Registration", messageType)
    }

    private fun replacePanelWithLoginPanel() {
        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
        if (parentToolWindow is JPanel) {
            parentToolWindow.removeAll()
            parentToolWindow.layout = BorderLayout()
            parentToolWindow.add(LoginPanel(project), BorderLayout.CENTER)
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
