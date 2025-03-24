//package com.example.sidebarplugin
//
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.project.Project
//import javax.swing.JOptionPane
//
//object ReviewActions {
//    fun handleReviewSelection(project: Project) {
//        val authToken = AuthTokenStorage.accessToken ?: ""
//        val activeFile = FileEditorManager.getInstance(project).selectedTextEditor?.virtualFile
//        val fileExtension = activeFile?.extension ?: "NA"
//        val language = mapFileExtensionToLanguage(fileExtension)
//
//        println("Detected language: $language")
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//        val selectedCode = FileEditorManager.getInstance(project).selectedTextEditor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        println("Access Token: $authToken")
//        println("Active file format: $language")
//        println("Git Info: $projectName, $branchName")
//        println("selected code: $selectedCode")
//
//        ApiUtils.sendReviewRequest(selectedCode, language, authToken, projectName, branchName)
//    }
//
//    private fun mapFileExtensionToLanguage(fileExtension: String): String {
//        return when (fileExtension.lowercase()) {
//            "py" -> "Python"
//            "java" -> "Java"
//            "kt" -> "Kotlin"
//            "js" -> "JavaScript"
//            "ts" -> "TypeScript"
//            "cpp", "cxx", "cc" -> "C++"
//            "c" -> "C"
//            "cs" -> "C#"
//            "rb" -> "Ruby"
//            "php" -> "PHP"
//            "swift" -> "Swift"
//            "go" -> "Go"
//            "rs" -> "Rust"
//            "html" -> "HTML"
//            "css" -> "CSS"
//            "sh" -> "Shell Script"
//            "json" -> "JSON"
//            "yaml", "yml" -> "YAML"
//            "xml" -> "XML"
//            "sql" -> "SQL"
//            else -> "Unknown"
//        }
//    }
//}
//
//
//
package com.example.sidebarplugin

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.JOptionPane

//object ReviewActions {
//    fun handleReviewSelection(project: Project, reviewType: String) {
//        val authToken = AuthTokenStorage.accessToken ?: ""
//        val activeFile = FileEditorManager.getInstance(project).selectedTextEditor?.virtualFile
//        val fileExtension = activeFile?.extension ?: "NA"
//        val language = mapFileExtensionToLanguage(fileExtension)
//
//        println("Detected language: $language")
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//        val selectedCode = FileEditorManager.getInstance(project).selectedTextEditor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        println("Access Token: $authToken")
//        println("Active file format: $language")
//        println("Git Info: $projectName, $branchName")
//        println("Selected code: $selectedCode")
//
//        val apiUrl = when (reviewType) {
//            "Performance" -> "http://34.123.3.28:3000/review/performance" // CK Review API
//            "Code Overall Review" -> "http://34.123.3.28:3000/review/overall" // Overall Code Review API
//            else -> {
//                println("Invalid review type selected.")
//                return
//            }
//        }
//
//        ApiUtils.sendReviewRequest(apiUrl, selectedCode, language, authToken, projectName, branchName)
//    }
//
//    private fun mapFileExtensionToLanguage(fileExtension: String): String {
//        return when (fileExtension.lowercase()) {
//            "py" -> "Python"
//            "java" -> "Java"
//            "kt" -> "Kotlin"
//            "js" -> "JavaScript"
//            "ts" -> "TypeScript"
//            "cpp", "cxx", "cc" -> "C++"
//            "c" -> "C"
//            "cs" -> "C#"
//            "rb" -> "Ruby"
//            "php" -> "PHP"
//            "swift" -> "Swift"
//            "go" -> "Go"
//            "rs" -> "Rust"
//            "html" -> "HTML"
//            "css" -> "CSS"
//            "sh" -> "Shell Script"
//            "json" -> "JSON"
//            "yaml", "yml" -> "YAML"
//            "xml" -> "XML"
//            "sql" -> "SQL"
//            else -> "Unknown"
//        }
//    }
//}
//
//
//

//working doubtwith line error
//object ReviewActions {
//    fun handleReviewSelection(project: Project, reviewType: String): String {
//        val authToken = AuthTokenStorage.accessToken ?: ""
//        val activeFile = FileEditorManager.getInstance(project).selectedTextEditor?.virtualFile
//        val fileExtension = activeFile?.extension ?: "NA"
//        val language = mapFileExtensionToLanguage(fileExtension)
//
//        val gitInfo = GitInfo.getGitInfo(project)
//        val projectName = gitInfo?.repositoryName ?: "NA"
//        val branchName = gitInfo?.currentBranch ?: "NA"
//        val selectedCode = FileEditorManager.getInstance(project).selectedTextEditor?.selectionModel?.selectedText ?: "No Code Selected"
//
//        val apiUrl = when (reviewType) {
//            "Performance" -> "http://34.123.3.28:3000/review/performance"
//            "Code Overall Review" -> "http://34.123.3.28:3000/review/overall"
//            else -> return "Invalid review type selected."
//        }
//
//        // Ensure it returns the actual response
//        return ApiUtils.sendReviewRequest(apiUrl, selectedCode, language, authToken, projectName, branchName)
//    }
//
//    private fun mapFileExtensionToLanguage(fileExtension: String): String {
//        return when (fileExtension.lowercase()) {
//            "py" -> "Python"
//            "java" -> "Java"
//            "kt" -> "Kotlin"
//            "js" -> "JavaScript"
//            "ts" -> "TypeScript"
//            "cpp", "cxx", "cc" -> "C++"
//            "c" -> "C"
//            "cs" -> "C#"
//            "rb" -> "Ruby"
//            "php" -> "PHP"
//            "swift" -> "Swift"
//            "go" -> "Go"
//            "rs" -> "Rust"
//            "html" -> "HTML"
//            "css" -> "CSS"
//            "sh" -> "Shell Script"
//            "json" -> "JSON"
//            "yaml", "yml" -> "YAML"
//            "xml" -> "XML"
//            "sql" -> "SQL"
//            else -> "Unknown"
//        }
//    }
//}
//
//
//

//import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBList
import javax.swing.*
import java.awt.Component
import kotlinx.serialization.*
import kotlinx.serialization.json.*

object ReviewActions {
    fun handleReviewSelection(project: Project, reviewType: String): String {
        val authToken = AuthTokenStorage.accessToken ?: ""
        val activeFile = FileEditorManager.getInstance(project).selectedTextEditor?.virtualFile
        val fileExtension = activeFile?.extension ?: "NA"
        val language = mapFileExtensionToLanguage(fileExtension)

        val gitInfo = GitInfo.getGitInfo(project)
        val projectName = gitInfo?.repositoryName ?: "NA"
        val branchName = gitInfo?.currentBranch ?: "NA"
        val selectedCode = FileEditorManager.getInstance(project).selectedTextEditor?.selectionModel?.selectedText ?: "No Code Selected"

        val apiUrl = when (reviewType) {
            "Performance" -> "http://34.46.36.105:3000/genieapi/review/performance"
            "Code Overall Review" -> "http://34.46.36.105:3000/genieapi/review/overall"
            else -> return "Invalid review type selected."
        }

        // Ensure it returns the actual response
        val response = ApiUtils.sendReviewRequest(apiUrl, selectedCode, language, authToken, projectName, branchName)
        println("************* $response")
        // Format the response as key-value pairs
        return formatJsonResponse(response)
    }

//    private fun formatJsonResponse(response: String): String {
//        return try {
//            // Parse the JSON string into a Kotlin object
//            val jsonElement = Json.parseToJsonElement(response)
//            val jsonObject = jsonElement.jsonObject
//
//            val formattedResponse = StringBuilder()
//            // Iterate through the JSON object and append key-value pairs to the result
//            jsonObject.forEach { (key, value) ->
//                formattedResponse.append("$key: $value\n")
//            }
//
//            formattedResponse.toString()
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message}"
//        }
//    }

//    private fun formatJsonResponse(response: String): String {
//        return try {
//            // Parse the JSON string into a Kotlin object
//            val jsonElement = Json.parseToJsonElement(response)
//            val jsonObject = jsonElement.jsonObject
//
//            // Remove the "innerMonologue" key if it exists
//            val filteredJsonObject = jsonObject.filterKeys { it != "innerMonologue" }
//
//            // Build the formatted string with the remaining key-value pairs
//            val formattedResponse = StringBuilder()
//            filteredJsonObject.forEach { (key, value) ->
//                formattedResponse.append("$key: $value\n")
//            }
//
//            formattedResponse.toString()
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message}"
//        }
//    }

//    private fun formatJsonResponse(response: String): String {
//        return try {
//            // Parse the JSON string into a Kotlin object
//            val jsonElement = Json.parseToJsonElement(response)
//            val jsonObject = jsonElement.jsonObject
//
//            // Remove the "innerMonologue" key if it exists
//            val filteredJsonObject = jsonObject.filterKeys { it != "innerMonologue" }
//
//            // Build the formatted string with the remaining key-value pairs
//            val formattedResponse = StringBuilder()
//
//            // Add key-value pairs from the main object
//            filteredJsonObject.forEach { (key, value) ->
//                formattedResponse.append("$key: $value\n")
//            }
//
//            // Check if "issues" key exists and process it if present
//            val issues = jsonObject["issues"]?.jsonArray
//            if (!issues.isNullOrEmpty()) {
//                formattedResponse.append("\nIssues:\n")
//                issues.forEachIndexed { index, issue ->
//                    if (issue is JsonObject) {
//                        issue.forEach { (key, value) ->
//                            formattedResponse.append("Issue ${index + 1} - $key: $value\n")
//                        }
//                    }
//                }
//            }
//
//            formattedResponse.toString()
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message}"
//        }
//    }

//    private fun formatJsonResponse(response: String): String {
//        return try {
//            // Parse the JSON string into a Kotlin object
//            val jsonElement = Json.parseToJsonElement(response)
//            val jsonObject = jsonElement.jsonObject
//
//            // Remove the "innerMonologue" key if it exists
//            val filteredJsonObject = jsonObject.filterKeys { it != "innerMonologue" }
//
//            // Build the formatted string with the remaining key-value pairs
//            val formattedResponse = StringBuilder()
//
//            // Add key-value pairs from the main object
//            filteredJsonObject.forEach { (key, value) ->
//                formattedResponse.append("$key: $value\n")
//            }
//
//            // Check if "issues" key exists and process it if present
//            val issues = jsonObject["issues"]?.jsonArray
//            if (!issues.isNullOrEmpty()) {
//                issues.forEachIndexed { index, issue ->
//                    if (issue is JsonObject) {
//                        formattedResponse.append("\nISSUE${index + 1}:\n")
//                        // Add each key-value pair for the issue
//                        issue.forEach { (key, value) ->
//                            formattedResponse.append("$key: $value\n")
//                        }
//                    }
//                }
//            }
//
//            formattedResponse.toString()
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message}"
//        }
//    }

    private fun formatJsonResponse(response: String): String {
        return try {
            // Parse the JSON string into a Kotlin object
            val jsonElement = Json.parseToJsonElement(response)
            val jsonObject = jsonElement.jsonObject

            // Remove the "innerMonologue" key if it exists
            val filteredJsonObject = jsonObject.filterKeys { it != "innerMonologue" && it != "issues" }

            // Build the formatted string with the remaining key-value pairs
            val formattedResponse = StringBuilder()

            // Add key-value pairs from the main object
            filteredJsonObject.forEach { (key, value) ->
                formattedResponse.append("$key: $value\n")
            }

            // Check if "issues" key exists and process it if present
            val issues = jsonObject["issues"]?.jsonArray
            if (!issues.isNullOrEmpty()) {
                issues.forEachIndexed { index, issue ->
                    if (issue is JsonObject) {
                        formattedResponse.append("\nISSUE${index + 1}\n")
                        // Add each key-value pair for the issue in a new line
                        issue.forEach { (key, value) ->
                            formattedResponse.append("$key: $value\n")
                        }
                    }
                }
            }

            formattedResponse.toString()
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message}"
        }
    }


    private fun mapFileExtensionToLanguage(fileExtension: String): String {
        return when (fileExtension.lowercase()) {
            "py" -> "Python"
            "java" -> "Java"
            "kt" -> "Kotlin"
            "js" -> "JavaScript"
            "ts" -> "TypeScript"
            "cpp", "cxx", "cc" -> "C++"
            "c" -> "C"
            "cs" -> "C#"
            "rb" -> "Ruby"
            "php" -> "PHP"
            "swift" -> "Swift"
            "go" -> "Go"
            "rs" -> "Rust"
            "html" -> "HTML"
            "css" -> "CSS"
            "sh" -> "Shell Script"
            "json" -> "JSON"
            "yaml", "yml" -> "YAML"
            "xml" -> "XML"
            "sql" -> "SQL"
            else -> "Unknown"
        }
    }
}

