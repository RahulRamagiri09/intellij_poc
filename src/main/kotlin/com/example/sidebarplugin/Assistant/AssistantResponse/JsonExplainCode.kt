package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.serialization.json.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import java.awt.*
import javax.swing.table.TableCellRenderer


object JsonExplainCode {
//    fun extractExplainCode(response: String): JPanel {
//        println("Raw API Response (JsonExplainCode): $response") // Debugging log
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
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            )
//            panel.add(JBScrollPane(createTable(arrayOf("Quality", "Remarks"), summaryData)))
//
//            // **Explanation Section**
//            val explanationLabel = JLabel("Explanation:")
//            explanationLabel.font = Font("Arial", Font.BOLD, 14)
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(explanationLabel)
//            panel.add(Box.createVerticalStrut(10))
//
//            val explanationData = jsonElement["explanation"]?.jsonArray?.map { explanation ->
//                val explanationObject = explanation.jsonObject
//                arrayOf(
//                    explanationObject["overview"]?.jsonPrimitive?.content ?: "N/A",
//                    explanationObject["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            } ?: emptyList()
//            panel.add(JBScrollPane(createTable(arrayOf("Overview", "Detailed Explanation"), explanationData)))
//
//            // **Key Components Section**
//            val keyComponentsLabel = JLabel("Key Components:")
//            keyComponentsLabel.font = Font("Arial", Font.BOLD, 14)
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(keyComponentsLabel)
//            panel.add(Box.createVerticalStrut(10))
//
//            val keyComponentsData = jsonElement["explanation"]?.jsonArray?.flatMap { explanation ->
//                explanation.jsonObject["keyComponents"]?.jsonArray?.map { component ->
//                    val compObj = component.jsonObject
//                    arrayOf(
//                        compObj["name"]?.jsonPrimitive?.content ?: "N/A",
//                        compObj["description"]?.jsonPrimitive?.content ?: "N/A"
//                    )
//                } ?: emptyList()
//            } ?: emptyList()
//            panel.add(JBScrollPane(createTable(arrayOf("Name", "Description"), keyComponentsData)))
//
//            // **Logic Flow Section**
//            val logicFlowLabel = JLabel("Logic Flow:")
//            logicFlowLabel.font = Font("Arial", Font.BOLD, 14)
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(logicFlowLabel)
//            panel.add(Box.createVerticalStrut(10))
//
//            val logicFlowData = jsonElement["explanation"]?.jsonArray?.flatMap { explanation ->
//                explanation.jsonObject["logicFlow"]?.jsonArray?.map { step ->
//                    val stepObj = step.jsonObject
//                    arrayOf(
//                        stepObj["step"]?.jsonPrimitive?.content ?: "N/A",
//                        stepObj["purpose"]?.jsonPrimitive?.content ?: "N/A"
//                    )
//                } ?: emptyList()
//            } ?: emptyList()
//            panel.add(JBScrollPane(createTable(arrayOf("Step", "Purpose"), logicFlowData)))
//
//            // **Algorithms Section**
//            val algorithmsLabel = JLabel("Algorithms:")
//            algorithmsLabel.font = Font("Arial", Font.BOLD, 14)
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(algorithmsLabel)
//            panel.add(Box.createVerticalStrut(10))
//
//            val algorithmsData = jsonElement["explanation"]?.jsonArray?.flatMap { explanation ->
//                explanation.jsonObject["algorithms"]?.jsonArray?.map { algo ->
//                    val algoObj = algo.jsonObject
//                    arrayOf(
//                        algoObj["name"]?.jsonPrimitive?.content ?: "N/A",
//                        algoObj["description"]?.jsonPrimitive?.content ?: "N/A"
//                    )
//                } ?: emptyList()
//            } ?: emptyList()
//            panel.add(JBScrollPane(createTable(arrayOf("Name", "Description"), algorithmsData)))
//
//            panel.preferredSize = Dimension(900, 700) // **Set medium size for panel**
//            panel
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }

    fun extractExplainCode(response: String): JPanel {
        println("Raw API Response (JsonExplainCode): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Set vertical layout for sections

            // **Summary Section**
            val summaryLabel = JLabel("Summary:")
            summaryLabel.font = Font("Arial", Font.BOLD, 14)
            panel.add(summaryLabel)
            panel.add(Box.createVerticalStrut(10))

            val summaryData = listOf(
                arrayOf(
                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
                )
            )
            panel.add(JBScrollPane(createTable(arrayOf("Quality", "Remarks"), summaryData)))

            // **Explanation Section**
            val explanationLabel = JLabel("Explanation:")
            explanationLabel.font = Font("Arial", Font.BOLD, 14)
            panel.add(Box.createVerticalStrut(20))
            panel.add(explanationLabel)
            panel.add(Box.createVerticalStrut(10))

            val explanationData = jsonElement["explanation"]?.jsonArray?.map { explanation ->
                val explanationObject = explanation.jsonObject
                arrayOf(
                    explanationObject["overview"]?.jsonPrimitive?.content ?: "N/A",
                    explanationObject["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
                )
            } ?: emptyList()
            panel.add(JBScrollPane(createTable(arrayOf("Overview", "Detailed Explanation"), explanationData)))

            // **Key Components Section**
            val keyComponentsLabel = JLabel("Key Components:")
            keyComponentsLabel.font = Font("Arial", Font.BOLD, 14)
            panel.add(Box.createVerticalStrut(20))
            panel.add(keyComponentsLabel)
            panel.add(Box.createVerticalStrut(10))

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
            val logicFlowLabel = JLabel("Logic Flow:")
            logicFlowLabel.font = Font("Arial", Font.BOLD, 14)
            panel.add(Box.createVerticalStrut(20))
            panel.add(logicFlowLabel)
            panel.add(Box.createVerticalStrut(10))

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
            val algorithmsLabel = JLabel("Algorithms:")
            algorithmsLabel.font = Font("Arial", Font.BOLD, 14)
            panel.add(Box.createVerticalStrut(20))
            panel.add(algorithmsLabel)
            panel.add(Box.createVerticalStrut(10))

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

            panel.preferredSize = Dimension(900, 700) // **Set medium size for panel**

            // ✅ Wrap everything in a scrollable panel to handle large data
            val scrollPane = JBScrollPane(panel).apply {
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
            }

            return JPanel(BorderLayout()).apply {
                add(scrollPane, BorderLayout.CENTER)
                preferredSize = Dimension(900, 700)
            }

        } catch (e: Exception) {
            errorPanel("Invalid JSON response: ${e.message}")
        }
    }


    // **Helper Method: Create Table with Multi-Line Renderer**
    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JBTable {
        val model = DefaultTableModel(columnNames, 0)
        data.forEach { model.addRow(it) }

        val table = JBTable(model)
        val renderer = MultiLineTableCellRenderer()

        for (i in 0 until table.columnCount) {
            table.columnModel.getColumn(i).cellRenderer = renderer
        }

        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.rowHeight = 100 // Adjust row height for better readability

        return table
    }

    // **Utility Method: Error Panel**
    private fun errorPanel(message: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
        panel.preferredSize = Dimension(800, 200) // **Set medium size for error panel**
        return panel
    }
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

