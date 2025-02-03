//package com.example.sidebarplugin
//import java.awt.*
//import javax.swing.*
//import java.io.InputStreamReader
//import java.io.BufferedReader
//
//import com.intellij.openapi.project.Project
//import java.net.HttpURLConnection
//import java.net.URL
////import org.json.JSONObject
//
//import kotlinx.serialization.*
//import kotlinx.serialization.json.*
//
//class LoginPanel(private val project: Project) : JPanel() {
//
//    private val emailField = JTextField(20).apply {
//        preferredSize = Dimension(200, 30)
//        maximumSize = Dimension(200, 30)
//    }
//    private val passwordField = JPasswordField(20).apply {
//        preferredSize = Dimension(200, 30)
//        maximumSize = Dimension(200, 30)
//    }
//
//    init {
//        layout = GridBagLayout()
//        val constraints = GridBagConstraints().apply {
//            insets = Insets(10, 10, 10, 10) // Padding between components
//            fill = GridBagConstraints.HORIZONTAL
//        }
//
//        // Email Label
//        constraints.gridx = 0
//        constraints.gridy = 0
//        constraints.anchor = GridBagConstraints.WEST
//        add(JLabel("Email:"), constraints)
//
//        // Email Field
//        constraints.gridx = 1
//        add(emailField, constraints)
//
//        // Password Label
//        constraints.gridx = 0
//        constraints.gridy = 1
//        add(JLabel("Password:"), constraints)
//
//        // Password Field
//        constraints.gridx = 1
//        add(passwordField, constraints)
//        emailField.text= "rahul97@gmail.com"
//
//        // Submit Button
//        constraints.gridx = 0
//        constraints.gridy = 2
//        constraints.gridwidth = 2
//        constraints.anchor = GridBagConstraints.CENTER
//        val submitButton = JButton("Submit").apply {
//            addActionListener {
//                val email = emailField.text.trim()
//                val password = String(passwordField.password).trim()
//
//                // Ensure both fields are filled
//                if (email.isEmpty() || password.isEmpty()) {
//                    showMessage("Email and Password cannot be empty.", JOptionPane.WARNING_MESSAGE)
//                } else {
//                    submitLoginForm(email, password)
//                }
//            }
//        }
//        add(submitButton, constraints)
//        passwordField.text="Test@123"
//    }
//
//    // Function to submit login form to API
//    private fun submitLoginForm(email: String, password: String) {
//        SwingUtilities.invokeLater {
//            try {
//                val apiUrl = "https://genie.bilvantis.in/fastapi/auth/login" // Replace with your API URL
//                val requestBody = "email=$email&password=$password"
//
//                val url = URL(apiUrl)
//                val connection = url.openConnection() as HttpURLConnection
//                connection.requestMethod = "POST"
//                connection.doOutput = true
//                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
//
//                connection.outputStream.use { it.write(requestBody.toByteArray()) }
//                val responseCode = connection.responseCode
//
//                if (responseCode == HttpURLConnection.HTTP_OK) {
//////                    showMessage("Login Successful!", JOptionPane.INFORMATION_MESSAGE)
////                    val responseMessage = connection.inputStream.bufferedReader().use { it.readText() }
////
////                    // Show the response in a dialog
////                    showMessage("Login Successful! Response: $responseMessage", JOptionPane.INFORMATION_MESSAGE)
////
////                    // Parse the JSON response to extract the token
//////                    val jsonResponse = JSONObject(responseMessage)
//////                    val accessToken = jsonResponse.getString("access_token")
////
////                    // Store the access token
//////                    AuthTokenStorage.accessToken = accessToken
////
////                    replacePanelWithSidebarToolWindow()
//                    val responseBody = BufferedReader(InputStreamReader(connection.inputStream)).readText()
//
//                    // Parse the JSON response using Kotlinx Serialization
//                    val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
//                    val accessToken = jsonResponse["access_token"]?.jsonPrimitive?.content
//
//                    // Store the access token
//                    if (accessToken != null) {
//                        AuthTokenStorage.accessToken = accessToken
//                        showMessage("Login Successful! Access Token Stored.", JOptionPane.INFORMATION_MESSAGE)
//                        println("Access Token Stored: ${AuthTokenStorage.accessToken}")
//                        replacePanelWithSidebarToolWindow()
//                    } else {
//                        showMessage("Error: No access token found", JOptionPane.ERROR_MESSAGE)
//                    }
//
//                } else {
//                    val errorMessage = BufferedReader(InputStreamReader(connection.errorStream)).readText()
//                    showMessage("Login Failed: $errorMessage", JOptionPane.ERROR_MESSAGE)
//                }
//
//                connection.disconnect()
//            } catch (e: Exception) {
//                showMessage("An error occurred: ${e.message}", JOptionPane.ERROR_MESSAGE)
//            }
//        }
//    }
//
//    // Function to display a message dialog
//    private fun showMessage(message: String, messageType: Int) {
//        JOptionPane.showMessageDialog(this, message, "Login Result", messageType)
//    }
//
//    // Function to replace LoginPanel with SidebarToolWindow
//    private fun replacePanelWithSidebarToolWindow() {
//        val parentToolWindow = SwingUtilities.getAncestorOfClass(JPanel::class.java, this)
//        if (parentToolWindow is JPanel) {
//            parentToolWindow.removeAll()
//            parentToolWindow.layout = BorderLayout()
//            parentToolWindow.add(SidebarToolWindow(project), BorderLayout.CENTER)
//            parentToolWindow.revalidate()
//            parentToolWindow.repaint()
//        }
//    }
//}
package com.example.sidebarplugin

import java.awt.*
import javax.swing.*
import java.io.InputStreamReader
import java.io.BufferedReader
import com.intellij.openapi.project.Project
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.RepositoryBuilder
import java.io.File

class LoginPanel(private val project: Project) : JPanel() {

    private val emailField = JTextField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }
    private val passwordField = JPasswordField(20).apply {
        preferredSize = Dimension(200, 30)
        maximumSize = Dimension(200, 30)
    }

    init {
        layout = GridBagLayout()
        val constraints = GridBagConstraints().apply {
            insets = Insets(10, 10, 10, 10) // Padding between components
            fill = GridBagConstraints.HORIZONTAL
        }


        // Email Label
        constraints.gridx = 0
        constraints.gridy = 0
        constraints.anchor = GridBagConstraints.WEST
        add(JLabel("Email:"), constraints)

        // Email Field
        constraints.gridx = 1
        add(emailField, constraints)

        // Password Label
        constraints.gridx = 0
        constraints.gridy = 1
        add(JLabel("Password:"), constraints)

        // Password Field
        constraints.gridx = 1
        add(passwordField, constraints)
        emailField.text = "rahul97@gmail.com"

        // Submit Button
        constraints.gridx = 0
        constraints.gridy = 2
        constraints.gridwidth = 2
        constraints.anchor = GridBagConstraints.CENTER
        val submitButton = JButton("Submit").apply {
            addActionListener {
                val email = emailField.text.trim()
                val password = String(passwordField.password).trim()

                // Ensure both fields are filled
                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Email and Password cannot be empty.", JOptionPane.WARNING_MESSAGE)
                } else {
                    submitLoginForm(email, password)
                }
            }
        }
        add(submitButton, constraints)
        passwordField.text = "Test@123"
    }

//    private fun getGitInfo(): String {
//        return try {
//            val repoDir = File(project.basePath)
//            val repository = RepositoryBuilder().setGitDir(File(repoDir, ".git")).readEnvironment().findGitDir().build()
//            val branch = repository.branch
//            val repoName = repository.directory.parentFile.name
//            "Repository: $repoName, Branch: $branch"
//        } catch (e: Exception) {
//            "Git info not available: ${e.message}"
//        }
//    }

    // Function to submit login form to API
    private fun submitLoginForm(email: String, password: String) {
        SwingUtilities.invokeLater {
            try {
                val apiUrl = "http://34.57.32.181:3000/fastapi/auth/login" // Replace with your API URL
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

                    // Parse the JSON response using Kotlinx Serialization
                    val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject
                    val accessToken = jsonResponse["access_token"]?.jsonPrimitive?.content

                    // Store the access token
                    if (accessToken != null) {
                        AuthTokenStorage.accessToken = accessToken
                        showMessage("Login Successful! Access Token Stored.", JOptionPane.INFORMATION_MESSAGE)
                        println("Access Token Stored: ${AuthTokenStorage.accessToken}")
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

    // Function to display a message dialog
    private fun showMessage(message: String, messageType: Int) {
        JOptionPane.showMessageDialog(this, message, "Login Result", messageType)
    }

    // Function to replace LoginPanel with SidebarToolWindow
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


