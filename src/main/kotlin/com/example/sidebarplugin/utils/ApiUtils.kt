package com.example.sidebarplugin.utils

import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object ApiUtils {
    fun sendReviewRequest(
        apiUrl: String,
        text: String,
        language: String,
        authToken: String,
        projectName: String,
        branchName: String
    ): String {
        return try {
            if (text.isBlank() || language.isBlank() || projectName.isBlank() || branchName.isBlank()) {
                return "Error: Missing required parameters."
            }

            val cleanedUrl = apiUrl.trim().trimEnd('/')
            val url = URL(cleanedUrl)

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $authToken")
            connection.doOutput = true

            val safeText = text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")

            val isKbms = cleanedUrl.endsWith("/kbmsapi/answer")

            if (isKbms) {
                val formParams = mapOf(
                    "question" to text,
                    "language" to language,
                    "project_name" to projectName,
                    "branch_name" to branchName,
                    "answer_config" to "chroma"
                )

                val formBody = formParams.entries.joinToString("&") { (key, value) ->
                    "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}"
                }

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")

                OutputStreamWriter(connection.outputStream, StandardCharsets.UTF_8).use {
                    it.write(formBody)
                    it.flush()
                }

            } else {
                connection.setRequestProperty("Content-Type", "application/json")

                val jsonInputString = when {
                    cleanedUrl.endsWith("/genieapi/assistant/code-generation") -> {
                        """
                        {
                            "prompt": "$safeText",
                            "language": "$language",
                            "project_name": "$projectName",
                            "branch_name": "$branchName"
                        }
                        """.trimIndent()
                    }

                    cleanedUrl.contains("/gitkbapi/get_code") || cleanedUrl.contains("/gitkbapi/explain") -> {
                        """
                        {
                            "question": "$safeText",
                            "language": "$language",
                            "project_name": "$projectName",
                            "branch_name": "$branchName"
                        }
                        """.trimIndent()
                    }

                    else -> {
                        """
                        {
                            "code": "$safeText",
                            "language": "$language",
                            "project_name": "$projectName",
                            "branch_name": "$branchName"
                        }
                        """.trimIndent()
                    }
                }

                connection.outputStream.use { os ->
                    os.write(jsonInputString.toByteArray(StandardCharsets.UTF_8))
                    os.flush()
                }
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                "Error: HTTP ${connection.responseCode}, " +
                        (connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error")
            }
        } catch (e: Exception) {
            "Exception: ${e.localizedMessage}"
        }
    }

    fun sendJobRequest(
        apiUrl: String,
        text: String,
        language: String,
        authToken: String,
        projectName: String,
        branchName: String
    ): String {
        return try {
            val url = URL(apiUrl.trim())
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $authToken")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val safeText = text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")

            val payload = """
            {
                "code": "$safeText",
                "language": "$language",
                "project_name": "$projectName",
                "branch_name": "$branchName"
            }
        """.trimIndent()

            connection.outputStream.use { os ->
                os.write(payload.toByteArray(StandardCharsets.UTF_8))
                os.flush()
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                val jobIdRegex = """"JobID"\s*:\s*"([^"]+)"""".toRegex()
                jobIdRegex.find(response)?.groupValues?.get(1) ?: "Error: JobID not found in response"
            } else {
                "Error: HTTP ${connection.responseCode}, " +
                        (connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error")
            }
        } catch (e: Exception) {
            "Exception: ${e.localizedMessage}"
        }
    }

    fun sendCancelJobRequest(apiUrl: String, jobId: String, authToken: String): String {
        return try {
            val url = URL(apiUrl.trim())
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Authorization", "Bearer $authToken")
            connection.setRequestProperty("Content-Type", "application/json")
            connection.doOutput = true

            val payload = """{"JobID": "$jobId"}"""

            connection.outputStream.use { os ->
                os.write(payload.toByteArray(StandardCharsets.UTF_8))
                os.flush()
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                connection.inputStream.bufferedReader().use { it.readText() }
            } else {
                "Error: HTTP ${connection.responseCode}, " +
                        (connection.errorStream?.bufferedReader()?.use { it.readText() } ?: "Unknown error")
            }
        } catch (e: Exception) {
            "Exception: ${e.localizedMessage}"
        }
    }

}
