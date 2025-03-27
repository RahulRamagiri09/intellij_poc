package com.example.sidebarplugin.Assistant.AssistantResponse

import kotlinx.serialization.json.*

object JsonLogging {
    fun extractAddLogging(response: String): String {
        println("Raw API Response (Logging): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return "Error: Response is not valid JSON: $response"
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            jsonElement["loggedCode"]?.jsonPrimitive?.content ?: "No refactored code available."
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }
    }
}
