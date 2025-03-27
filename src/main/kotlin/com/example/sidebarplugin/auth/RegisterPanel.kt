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

class RegisterPanel(private val project: Project) : JPanel() {
    private val usernameField = JTextField(20)
    private val fullNameField = JTextField(20)
    private val emailField = JTextField(20)
    private val passwordField = JPasswordField(20)
    private val confirmPasswordField = JPasswordField(20)
    private val companyNameField = JTextField(20)
    private val loginButton = JButton("Login") // Always present Login button
    private val urlState = ServiceManager.getService(PersistentState::class.java)

    init {
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            insets = Insets(10, 10, 10, 10)
            fill = GridBagConstraints.HORIZONTAL
        }

        // UI Fields
        addField("Username:", usernameField, constraints, 0)
        addField("Full Name:", fullNameField, constraints, 1)
        addField("Email:", emailField, constraints, 2)
        addField("Password:", passwordField, constraints, 3)
        addField("Confirm Password:", confirmPasswordField, constraints, 4)
        addField("Company Name:", companyNameField, constraints, 5)

        // Register Button
        constraints.gridx = 0
        constraints.gridy = 6
        constraints.gridwidth = 2
        constraints.anchor = GridBagConstraints.CENTER
        val registerButton = JButton("Submit").apply {
            addActionListener {
                val username = usernameField.text.trim()
                val fullName = fullNameField.text.trim()
                val email = emailField.text.trim()
                val password = String(passwordField.password).trim()
                val confirmPassword = String(confirmPasswordField.password).trim()
                val companyName = companyNameField.text.trim()

                if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || companyName.isEmpty()) {
                    showMessage("All fields are required.", JOptionPane.WARNING_MESSAGE)
                } else if (password != confirmPassword) {
                    showMessage("Passwords do not match.", JOptionPane.ERROR_MESSAGE)
                } else {
                    submitRegisterForm(username, fullName, email, password, confirmPassword, companyName)
                }
            }
        }
        add(registerButton, constraints)

        // Always visible Login Button
        constraints.gridy = 7
        loginButton.addActionListener {
            replacePanelWithLoginPanel()
        }
        add(loginButton, constraints)
    }

    private fun addField(label: String, field: JComponent, constraints: GridBagConstraints, row: Int) {
        constraints.gridx = 0
        constraints.gridy = row
        add(JLabel(label), constraints)
        constraints.gridx = 1
        add(field, constraints)
    }

    private fun submitRegisterForm(username: String, fullName: String, email: String, password: String, confirmPassword: String, companyName: String) {
        SwingUtilities.invokeLater {
            try {
                val apiUrl = "http://34.46.36.105:3000/genieapi/auth/register"
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
                    val responseBody = BufferedReader(InputStreamReader(connection.inputStream)).readText()
                    val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

                    JOptionPane.showMessageDialog(this, "Registration Successful! Please proceed to login....", "Success", JOptionPane.INFORMATION_MESSAGE)

                    replacePanelWithLoginPanel() // Directly navigate to login after clicking OK
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

    //    private fun showMessage(message: String, messageType: Int) {
//        JOptionPane.showMessageDialog(this, message, "Registration Result", messageType)
//    }
    private fun showMessage(message: String, messageType: Int) {
        val dialog = JDialog(SwingUtilities.getWindowAncestor(this), "Registration", Dialog.ModalityType.APPLICATION_MODAL)
        dialog.layout = BorderLayout()

        // Create a panel with padding
        val messagePanel = JPanel()
        messagePanel.layout = BorderLayout()
        messagePanel.border = BorderFactory.createEmptyBorder(20, 30, 20, 30) // Padding (top, left, bottom, right)

        val messageLabel = JLabel(message)
        messageLabel.horizontalAlignment = SwingConstants.CENTER

        messagePanel.add(messageLabel, BorderLayout.CENTER)
        dialog.add(messagePanel, BorderLayout.CENTER)

        val buttonPanel = JPanel()
        buttonPanel.layout = FlowLayout(FlowLayout.CENTER)

        val okButton = JButton("OK")
        okButton.addActionListener {
            dialog.dispose()
            if (messageType == JOptionPane.INFORMATION_MESSAGE) {
                replacePanelWithLoginPanel() // Navigate to login after success
            }
        }

        buttonPanel.add(okButton)
        dialog.add(buttonPanel, BorderLayout.SOUTH)

        dialog.pack()
        dialog.setLocationRelativeTo(this)
        dialog.isVisible = true
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
        println("Navigating to Login Panel")
    }
}

