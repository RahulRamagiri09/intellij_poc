//working correct
package com.example.sidebarplugin
import java.net.HttpURLConnection
import java.net.URL

object ApiUtils {
    fun sendReviewRequest(apiUrl: String, text: String, language: String, authToken: String, projectName: String, branchName: String): String {
        return try {
            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $authToken")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val safeText = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                .replace("\r", "\\r").replace("\t", "\\t")

            if (safeText.isBlank() || language.isBlank() || projectName.isBlank() || branchName.isBlank()) {
                return "Error: Missing required parameters."
            }

            val jsonInputString = """
                {
                    "code": "$safeText",
                    "language": "$language",
                    "project_name": "$projectName",
                    "branch_name": "$branchName"
                }
            """.trimIndent()

            connection.outputStream.use { os ->
                os.write(jsonInputString.toByteArray(Charsets.UTF_8))
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                "Error: HTTP ${connection.responseCode}, ${connection.errorStream.bufferedReader().use { it.readText() }}"
            }
        } catch (e: Exception) {
            "Exception: ${e.localizedMessage}"
        }
    }
}
