package com.example.sidebarplugin.utils

import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object ApiUtils {
    fun sendReviewRequest(apiUrl: String, text: String, language: String, authToken: String, projectName: String, branchName: String): String {
        return try {
            if (text.isBlank() || language.isBlank() || projectName.isBlank() || branchName.isBlank()) {
                return "Error: Missing required parameters."
            }

            val url = URL(apiUrl)
            println("******* api url, $apiUrl, $url")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $authToken")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val safeText = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")

            // Use "prompt" if URL matches, else use "code"
            val jsonInputString = if (apiUrl == "http://34.46.36.105:3000/genieapi/assistant/code-generation") {
                """
                {
                    "prompt": "$safeText",
                    "language": "$language",
                    "project_name": "$projectName",
                    "branch_name": "$branchName"
                }
                """.trimIndent()
            } else {
                """
                {
                    "code": "$safeText",
                    "language": "$language",
                    "project_name": "$projectName",
                    "branch_name": "$branchName"
                }
                """.trimIndent()
            }

            connection.outputStream.use { os ->
                os.write(jsonInputString.toByteArray(StandardCharsets.UTF_8))
                os.flush()
            }

            return if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                "Error: HTTP ${connection.responseCode}, " +
                connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error"
            }
        } catch (e: Exception) {
            "Exception: ${e.localizedMessage}"
        }
    }
}


