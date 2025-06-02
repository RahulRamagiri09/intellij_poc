package com.example.sidebarplugin

import com.example.sidebarplugin.Review.ReviewActions
import com.example.sidebarplugin.utils.IconUtils
import com.intellij.openapi.project.Project
import javax.swing.*
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

object DropDownMenuReview {
    fun createDropdownMenu(title: String, items: List<String>, project: Project): JPanel {
        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        val iconMap = mapOf(
            "Overall Review" to IconUtils.load("Review.svg")
        )

        items.forEach { item ->
            val label = JLabel(item, iconMap[item], JLabel.LEFT)
            label.iconTextGap = 8
            label.alignmentX = Component.LEFT_ALIGNMENT
            label.border = BorderFactory.createEmptyBorder(4, 4, 4, 4)
            label.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)

            label.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent?) {
                    ReviewActions.handleReviewRequest(project, item)
                }
            })

            dropdownPanel.add(label)
        }

        return dropdownPanel
    }
}

object LanguageDetectUtils {
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
