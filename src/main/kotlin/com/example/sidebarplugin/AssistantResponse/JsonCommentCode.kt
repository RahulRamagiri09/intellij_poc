package com.example.sidebarplugin.AssistantResponse

import kotlinx.serialization.json.*

object JsonCommentCode {
    fun extractCommentCode(response: String): String {
        println("Raw API Response (JsonCommentCode): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return "Error: Response is not valid JSON: $response"
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            jsonElement["commentedCode"]?.jsonPrimitive?.content ?: "No refactored code available."
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }
    }
}
