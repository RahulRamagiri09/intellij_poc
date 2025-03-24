package com.example.sidebarplugin

object AssistantUtils {
    fun mapFileExtensionToLanguages(fileExtension: String): String {
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
