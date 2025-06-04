package com.example.sidebarplugin.KB.KBResponse
import kotlinx.serialization.json.*

object JsonGetResponseFromKB {
    fun extractGetResponseFromKB(response: String): String {
        println("Raw API Response (Get Code): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return "Error: Response is not valid JSON: $response"
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            jsonElement["response"]?.jsonPrimitive?.content ?: "No Get Code added."
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }
    }
}

