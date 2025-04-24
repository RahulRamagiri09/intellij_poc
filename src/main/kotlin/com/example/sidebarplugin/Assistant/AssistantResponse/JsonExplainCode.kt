package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.serialization.json.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import javax.swing.table.TableCellRenderer

object JsonExplainCode {
    fun extractExplainCode(response: String): JPanel {
        println("Raw API Response (JsonExplainCode): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Set vertical layout for sections background

            // **Summary Section**
            panel.add(createSectionLabel("Summary:"))
            val summaryData = listOf(
                arrayOf(
                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
                )
            )
            panel.add(JBScrollPane(createTable(arrayOf("Quality", "Remarks"), summaryData)))

            // **Explanation Section**
            panel.add(Box.createVerticalStrut(20))
            panel.add(createSectionLabel("Explanation:"))
            val explanationData = jsonElement["explanation"]?.jsonArray?.map { explanation ->
                val explanationObject = explanation.jsonObject
                arrayOf(
                    explanationObject["overview:"]?.jsonPrimitive?.content ?: "N/A",
                    explanationObject["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
                )
            } ?: emptyList()
            panel.add(JBScrollPane(createTable(arrayOf("Overview", "Detailed Explanation"), explanationData)))

            // **Key Components Section**
            panel.add(Box.createVerticalStrut(20))
            panel.add(createSectionLabel("Key Components:"))
            val keyComponentsData = jsonElement["explanation"]?.jsonArray?.flatMap { explanation ->
                explanation.jsonObject["keyComponents"]?.jsonArray?.map { component ->
                    val compObj = component.jsonObject
                    arrayOf(
                        compObj["name"]?.jsonPrimitive?.content ?: "N/A",
                        compObj["description"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } ?: emptyList()
            } ?: emptyList()
            panel.add(JBScrollPane(createTable(arrayOf("Name", "Description"), keyComponentsData)))

            // **Logic Flow Section**
            panel.add(Box.createVerticalStrut(20))
            panel.add(createSectionLabel("Logic Flow:"))
            val logicFlowData = jsonElement["explanation"]?.jsonArray?.flatMap { explanation ->
                explanation.jsonObject["logicFlow"]?.jsonArray?.map { step ->
                    val stepObj = step.jsonObject
                    arrayOf(
                        stepObj["step"]?.jsonPrimitive?.content ?: "N/A",
                        stepObj["purpose"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } ?: emptyList()
            } ?: emptyList()
            panel.add(JBScrollPane(createTable(arrayOf("Step", "Purpose"), logicFlowData)))

            // **Algorithms Section**
            panel.add(Box.createVerticalStrut(20))
            panel.add(createSectionLabel("Algorithms:"))
            val algorithmsData = jsonElement["explanation"]?.jsonArray?.flatMap { explanation ->
                explanation.jsonObject["algorithms"]?.jsonArray?.map { algo ->
                    val algoObj = algo.jsonObject
                    arrayOf(
                        algoObj["name"]?.jsonPrimitive?.content ?: "N/A",
                        algoObj["description"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } ?: emptyList()
            } ?: emptyList()
            panel.add(JBScrollPane(createTable(arrayOf("Name", "Description"), algorithmsData)))

            // Wrap the entire panel in an outer scrollable pane
            val outerScrollPane = JBScrollPane(panel).apply {
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
//                background = Color.WHITE
            }

            return JPanel(BorderLayout()).apply {
                add(outerScrollPane, BorderLayout.CENTER)
//                background = Color.WHITE
//                preferredSize = Dimension(900, 900)
                preferredSize = Dimension(1200, 600)
            }

        } catch (e: Exception) {
            errorPanel("Invalid JSON response: ${e.message}")
        }
    }

    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JBTable {
        val model = DefaultTableModel(columnNames, 0)
        data.forEach { model.addRow(it) }

        val table = JBTable(model)
        val renderer = MultiLineTableCellRenderer()

        // Set Custom Renderer for Multi-Line Support
        for (i in 0 until table.columnCount) {
            table.columnModel.getColumn(i).cellRenderer = renderer
        }

        // Set bold header renderer with custom background color
        val headerFont = Font("Arial", Font.BOLD, 14)
        val headerRenderer = object : JLabel(), TableCellRenderer {
            init {
                font = headerFont
                horizontalAlignment = JLabel.CENTER
                isOpaque = true
                background = Color(0x07, 0x43, 0x9C) // Custom color #07439C
                foreground = Color.WHITE // White text for contrast
            }

            override fun getTableCellRendererComponent(
                table: JTable, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int
            ): Component {
                text = value?.toString() ?: ""
                return this
            }
        }

        table.tableHeader.defaultRenderer = headerRenderer
        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.rowHeight = 150 // Fixed height for all rows

        return table
    }

    // **Utility Method: Error Panel**
    private fun errorPanel(message: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
        panel.preferredSize = Dimension(800, 200) // **Set medium size for error panel**
//        panel.preferredSize = Dimension(1200, 600)
        return panel
    }

    // **Helper Method: Create Section Title Label**
    private fun createSectionLabel(title: String): JLabel {
        val label = JLabel(title)
        label.font = Font("Arial", Font.BOLD, 16 )
        return label
    }
}

//// **Custom Cell Renderer for Multi-Line Text**
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
