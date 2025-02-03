//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.Serializable
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.io.OutputStream
//import java.net.HttpURLConnection
//import java.net.URL
//
//@Serializable
//data class ReviewRequest(
//    val code: String,
//    val language: String,
//    val project_name: String,
//    val branch_name: String
//)
//
//private fun sendReviewRequest(code: String, language: String, projectName: String, branchName: String, authToken: String) {
//    CoroutineScope(Dispatchers.IO).launch {
//        try {
//            val url = URL("http://your-api-url/review/overall")
//            val connection = url.openConnection() as HttpURLConnection
//
//            connection.requestMethod = "POST"
//            connection.setRequestProperty("Authorization", "Bearer $authToken")
//            connection.setRequestProperty("Content-Type", "application/json")
//            connection.doOutput = true
//
//            val requestBody = Json.encodeToString(ReviewRequest(code, language, projectName, branchName))
//
//            // Send request body
//            connection.outputStream.use { outputStream: OutputStream ->
//                outputStream.write(requestBody.toByteArray(Charsets.UTF_8))
//                outputStream.flush()
//            }
//
//            // Read response
//            val responseCode = connection.responseCode
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
//                    val response = reader.readText()
//                    println("Response: $response")
//                }
//            } else {
//                println("Error: HTTP $responseCode")
//            }
//
//            connection.disconnect()
//        } catch (e: Exception) {
//            println("Request failed: ${e.message}")
//        }
//    }
//}

package com.example.sidebarplugin

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.net.HttpURLConnection
import java.net.URL

fun sendReviewRequest(text: String, language: String, authToken: String, projectName: String, branchName: String) {
    try {
        val url = URL("http://34.123.3.28:3000/fastapi/review/overall") // Replace with actual API URL
        println("enter to api")
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "POST"
        connection.setRequestProperty("Authorization", "Bearer $authToken")
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val safeText = text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")

        if (safeText.isBlank() || language.isBlank() || projectName.isBlank() || branchName.isBlank()) {
            println("Error: Missing required parameters.")
            return
        }


        val requestBody = """
            {
                "code": "$safeText",
                "language": "$language",
                "project_name": "$projectName",
                "branch_name": "$branchName"
            }
        """.trimIndent()
        println("Request Body: $requestBody")


        connection.outputStream.use { it.write(requestBody.toByteArray()) }

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }
            println("Response: $response")
        } else {
//            println("Error: HTTP ${connection.responseCode}")
            val errorResponse = connection.errorStream.bufferedReader().use { it.readText() }
            println("Error: HTTP ${connection.responseCode}, $errorResponse")
        }

        connection.disconnect()
    } catch (e: Exception) {
        println("API Request Failed: ${e.message}")
    }
}
