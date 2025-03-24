package com.example.sidebarplugin

import kotlinx.serialization.json.*

object JsonUtils {
    fun extractDocumentation(response: String, assistantType: String): String {
        println("Raw API Response: $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return "Error: Response is not valid JSON: $response"
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject

            when (assistantType) {
                "Doc String" -> jsonElement["documentationAdded"]?.jsonPrimitive?.content ?: "No documentation added."
                "Refactor" -> jsonElement["refactoredCode"]?.jsonPrimitive?.content ?: "No refactored code available."
                else -> "Invalid assistant type."
            }
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }
    }
}
