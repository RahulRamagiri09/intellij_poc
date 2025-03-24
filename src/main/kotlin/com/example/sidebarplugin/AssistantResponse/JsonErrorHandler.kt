package com.example.sidebarplugin.AssistantResponse

import kotlinx.serialization.json.*

object JsonErrorHandler {
    fun extractErrorHandlerCode(response: String): String {
        println("Raw API Response (ErrorHandler): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return "Error: Response is not valid JSON: $response"
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            jsonElement["exceptionHandlingAdded"]?.jsonPrimitive?.content ?: "No refactored code available."
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }
    }
}
