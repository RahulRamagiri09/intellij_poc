//package com.example.sidebarplugin.Assistant.AssistantResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import java.awt.*
//import javax.swing.table.TableCellRenderer
//
//object JsonOverallReview {
//    fun extractOverallReview(response: String): JPanel {
//        println("Raw API Response (JsonOverallReview): $response") // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return errorPanel("Error: Response is not valid JSON")
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            val panel = JPanel()
//            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Set vertical layout for sections
//
//            // **Summary Section**
//            val summaryLabel = JLabel("Summary:")
//            summaryLabel.font = Font("Arial", Font.BOLD, 14)
//            panel.add(summaryLabel)
//            panel.add(Box.createVerticalStrut(10))
//
//            val summaryData = listOf(
//                arrayOf(
//                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["overallSeverity"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            )
//            panel.add(JBScrollPane(createTable(arrayOf("Quality", "Remarks", "Severity"), summaryData)))
//
//            // **Issues Section**
//            val issuesLabel = JLabel("Issues Breakdown:")
//            issuesLabel.font = Font("Arial", Font.BOLD, 14)
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(issuesLabel)
//            panel.add(Box.createVerticalStrut(10))
//
//            val issues = jsonElement["issues"]?.jsonObject
//            val issueData = mutableListOf<Array<String>>()
//
//            issues?.forEach { (category, issuesArray) ->
//                issuesArray.jsonArray.forEach { issue ->
//                    val issueObj = issue.jsonObject
//                    issueData.add(
//                        arrayOf(
//                            category, // Category Name (e.g., Quality, Performance)
//                            issueObj["identification"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["explanation"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["fix"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["score"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["severity"]?.jsonPrimitive?.content ?: "N/A"
//                        )
//                    )
//                }
//            }
//            panel.add(JBScrollPane(createTable(arrayOf("Category", "Issue", "Explanation", "Fix", "Score", "Severity"), issueData)))
//
//            panel.preferredSize = Dimension(1200, 700) // Set medium size for panel
//            panel
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }
//
//    // **Helper Method: Create Table with Multi-Line Renderer**
//    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JBTable {
//        val model = DefaultTableModel(columnNames, 0)
//        data.forEach { model.addRow(it) }
//
//        val table = JBTable(model)
//        val renderer = MultiLineTableCellRendererss()
//
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer
//        }
//
//        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
//        table.rowHeight = 50 // Adjust row height for better readability
//
//        return table
//    }
//
//    // **Utility Method: Error Panel**
//    private fun errorPanel(message: String): JPanel {
//        val panel = JPanel(BorderLayout())
//        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
//        panel.preferredSize = Dimension(800, 200) // Set medium size for error panel
//        return panel
//    }
//}
//
//// **Custom Cell Renderer for Multi-Line Text**
//class MultiLineTableCellRendererss : JTextArea(), TableCellRenderer {
//    init {
//        lineWrap = true
//        wrapStyleWord = true
//        isOpaque = true
//    }
//
//    override fun getTableCellRendererComponent(
//        table: JTable,
//        value: Any?,
//        isSelected: Boolean,
//        hasFocus: Boolean,
//        row: Int,
//        column: Int
//    ): Component {
//        text = value?.toString() ?: ""
//        return this
//    }
//}


////
//package com.example.sidebarplugin.Assistant.AssistantResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import java.awt.*
//import javax.swing.table.TableCellRenderer
//
//object JsonOverallReview {
//    fun extractOverallReview(response: String): JPanel {
//        println("Raw API Response (JsonOverallReview): $response") // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return errorPanel("Error: Response is not valid JSON")
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            val panel = JPanel()
//            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Set vertical layout
//
//            // **Summary Table**
//            panel.add(createSectionLabel("Summary"))
//            val summaryData = listOf(
//                arrayOf(
//                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["overallSeverity"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            )
//            panel.add(JBScrollPane(createTable(arrayOf("Quality", "Remarks", "Severity"), summaryData)))
//
//            // **Issue Tables for Different Categories**
//            val issues = jsonElement["issues"]?.jsonObject
//            val categoryOrder = listOf("Quality", "Performance", "Security", "Organization Standards", "CK Metrics", "Syntax")
//
//            categoryOrder.forEach { category ->
//                val issuesArray = issues?.get(category)?.jsonArray
//                if (issuesArray != null && issuesArray.isNotEmpty()) {
//                    panel.add(Box.createVerticalStrut(20)) // Add spacing
//                    panel.add(createSectionLabel(category)) // Section Title
//
//                    val issueData = issuesArray.map { issue ->
//                        val issueObj = issue.jsonObject
//                        arrayOf(
//                            issueObj["identification"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["explanation"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["fix"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["score"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["severity"]?.jsonPrimitive?.content ?: "N/A"
//                        )
//                    }
//
//                    panel.add(JBScrollPane(createTable(arrayOf("Issue", "Explanation", "Fix", "Score", "Severity"), issueData)))
//                }
//            }
//
//            panel.preferredSize = Dimension(1200, 700) // Set medium size for panel
//            panel
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }
//
//    // **Helper Method: Create Table with Multi-Line Renderer**
//    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JBTable {
//        val model = DefaultTableModel(columnNames, 0)
//        data.forEach { model.addRow(it) }
//
//        val table = JBTable(model)
//        val renderer = MultiLineTableCellRendererss()
//
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer
//        }
//
//        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
//        table.rowHeight = 50 // Adjust row height for better readability
//
//        return table
//    }
//
//    // **Utility Method: Error Panel**
//    private fun errorPanel(message: String): JPanel {
//        val panel = JPanel(BorderLayout())
//        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
//        panel.preferredSize = Dimension(800, 200)
//        return panel
//    }
//
//    // **Custom Cell Renderer for Multi-Line Text**
//    class MultiLineTableCellRendererss : JTextArea(), TableCellRenderer {
//        init {
//            lineWrap = true
//            wrapStyleWord = true
//            isOpaque = true
//        }
//
//        override fun getTableCellRendererComponent(
//            table: JTable,
//            value: Any?,
//            isSelected: Boolean,
//            hasFocus: Boolean,
//            row: Int,
//            column: Int
//        ): Component {
//            text = value?.toString() ?: ""
//            return this
//        }
//    }
//
//    // **Helper Method: Section Title Label**
//    private fun createSectionLabel(title: String): JLabel {
//        val label = JLabel(title)
//        label.font = Font("Arial", Font.BOLD, 16)
//        label.alignmentX = Component.LEFT_ALIGNMENT
//        return label
//    }
//}
//


package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.serialization.json.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import javax.swing.table.TableCellRenderer

object JsonOverallReview {
    fun extractOverallReview(response: String): JPanel {
        println("Raw API Response (JsonOverallReview): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Set vertical layout

            // **Summary Table**
            panel.add(createSectionLabel("Summary"))
            val summaryData = listOf(
                arrayOf(
                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["overallSeverity"]?.jsonPrimitive?.content ?: "N/A"
                )
            )
            panel.add(createTable(arrayOf("Quality", "Remarks", "Severity"), summaryData))
            panel.add(Box.createVerticalStrut(15)) // Spacing

            // **Issue Tables for Different Categories**
            val issues = jsonElement["issues"]?.jsonObject
            val categoryOrder = listOf("Quality", "Performance", "Security", "Organization Standards", "CK Metrics", "Syntax")

            categoryOrder.forEach { category ->
                val issuesArray = issues?.get(category)?.jsonArray
                if (issuesArray != null && issuesArray.isNotEmpty()) {
                    panel.add(Box.createVerticalStrut(20)) // Add spacing
                    panel.add(createSectionLabel(category)) // Section Title

                    val issueData = issuesArray.map { issue ->
                        val issueObj = issue.jsonObject
                        arrayOf(
                            issueObj["identification"]?.jsonPrimitive?.content ?: "N/A",
                            issueObj["explanation"]?.jsonPrimitive?.content ?: "N/A",
                            issueObj["fix"]?.jsonPrimitive?.content ?: "N/A",
                            issueObj["severity"]?.jsonPrimitive?.content ?: "N/A"
                        )
                    }

                    panel.add(createTable(arrayOf("Issue", "Explanation", "Fix", "Severity"), issueData))
                }
            }

            // **Wrap the ENTIRE panel inside a scroll pane**
            val scrollPane = JBScrollPane(panel)
            scrollPane.preferredSize = Dimension(1200, 600)

            val containerPanel = JPanel(BorderLayout())
            containerPanel.preferredSize = Dimension(1200, 600)
            containerPanel.add(scrollPane, BorderLayout.CENTER)

            containerPanel
        } catch (e: Exception) {
            errorPanel("Invalid JSON response: ${e.message}")
        }
    }

    // **Helper Method: Create Table with Multi-Line Renderer**
    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JPanel {
        val model = DefaultTableModel(columnNames, 0)
        data.forEach { model.addRow(it) }

        val table = JBTable(model)
        val renderer = MultiLineTableCellRenderer()

        for (i in 0 until table.columnCount) {
            table.columnModel.getColumn(i).cellRenderer = renderer
        }

        table.autoResizeMode = JTable.AUTO_RESIZE_OFF // Prevent auto shrinking

        // 🔹 Set column widths for better readability
        if (table.columnCount >= 5) {
            table.columnModel.getColumn(0).preferredWidth = 500  // Issue
            table.columnModel.getColumn(1).preferredWidth = 300  // Explanation
            table.columnModel.getColumn(2).preferredWidth = 500  // Fix
            table.columnModel.getColumn(4).preferredWidth = 80   // Severity
        }

        table.rowHeight = 150 // Adjust row height for better readability
        table.tableHeader.reorderingAllowed = false // Prevent column reordering

        val tablePanel = JPanel(BorderLayout())
        tablePanel.add(table.tableHeader, BorderLayout.NORTH) // Ensure header is visible
        tablePanel.add(table, BorderLayout.CENTER)

        return tablePanel
    }

    // **Utility Method: Error Panel**
    private fun errorPanel(message: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
        panel.preferredSize = Dimension(800, 200) // Set medium size for error panel
        return panel
    }

    // **Custom Cell Renderer for Multi-Line Text**
    class MultiLineTableCellRenderer : JTextArea(), TableCellRenderer {
        init {
            lineWrap = true
            wrapStyleWord = true
            isOpaque = true
        }

        override fun getTableCellRendererComponent(
            table: JTable,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ): Component {
            text = value?.toString() ?: ""
            return this
        }
    }

    // **Helper Method: Section Title Label**
    private fun createSectionLabel(title: String): JPanel {
        val label = JLabel(title)
        label.font = Font("Arial", Font.BOLD, 16)

        val wrapper = JPanel()
        wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS)
        wrapper.add(Box.createVerticalStrut(10)) // Top spacing
        wrapper.add(label)
        wrapper.add(Box.createVerticalStrut(5)) // Bottom spacing

        return wrapper
    }
}
