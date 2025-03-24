package com.example.sidebarplugin.AssistantResponse

import kotlinx.serialization.json.*

object JsonDocString {
    fun extractDocumentation(response: String): String {
        println("Raw API Response (Doc String): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return "Error: Response is not valid JSON: $response"
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            jsonElement["documentationAdded"]?.jsonPrimitive?.content ?: "No documentation added."
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }
    }
}
