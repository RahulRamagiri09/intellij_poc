//package com.example.sidebarplugin.GitKB.GitKBResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import java.awt.*
//import javax.swing.table.TableCellRenderer
//
//object JsonExplain {
//    fun extractExplain(response: String): JPanel {
//        println("Raw API Response (JsonExplainCode): $response") // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return errorPanel("Error: Response is not valid JSON")
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            val panel = JPanel()
//            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Set vertical layout for sections background
//
//            // **Summary Section**
//            panel.add(createSectionLabel("Summary:"))
//            val summaryData = listOf(
//                arrayOf(
//                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            )
//            panel.add(JBScrollPane(createTable(arrayOf("Quality", "Remarks"), summaryData)))
//
//            // **Explanation Section**
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(createSectionLabel("Explanation:"))
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
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(createSectionLabel("Key Components:"))
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
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(createSectionLabel("Logic Flow:"))
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
//            panel.add(Box.createVerticalStrut(20))
//            panel.add(createSectionLabel("Algorithms:"))
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
//            // Wrap the entire panel in an outer scrollable pane
//            val outerScrollPane = JBScrollPane(panel).apply {
//                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
//                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
////                background = Color.WHITE
//            }
//
//            return JPanel(BorderLayout()).apply {
//                add(outerScrollPane, BorderLayout.CENTER)
////                background = Color.WHITE
////                preferredSize = Dimension(900, 900)
//                preferredSize = Dimension(1200, 600)
//            }
//
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }
//
//    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JBTable {
//        val model = DefaultTableModel(columnNames, 0)
//        data.forEach { model.addRow(it) }
//
//        val table = JBTable(model)
//        val renderer = MultiLineTableCellRenderer()
//
//        // Set Custom Renderer for Multi-Line Support
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer
//        }
//
//        // Set bold header renderer with custom background color
//        val headerFont = Font("Arial", Font.BOLD, 14)
//        val headerRenderer = object : JLabel(), TableCellRenderer {
//            init {
//                font = headerFont
//                horizontalAlignment = JLabel.CENTER
//                isOpaque = true
//                background = Color(0x07, 0x43, 0x9C) // Custom color #07439C
//                foreground = Color.WHITE // White text for contrast
//            }
//
//            override fun getTableCellRendererComponent(
//                table: JTable, value: Any?, isSelected: Boolean, hasFocus: Boolean, row: Int, column: Int
//            ): Component {
//                text = value?.toString() ?: ""
//                return this
//            }
//        }
//
//        table.tableHeader.defaultRenderer = headerRenderer
//        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
//        table.rowHeight = 150 // Fixed height for all rows
//
//        return table
//    }
//
//    // **Utility Method: Error Panel**
//    private fun errorPanel(message: String): JPanel {
//        val panel = JPanel(BorderLayout())
//        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
//        panel.preferredSize = Dimension(800, 200) // **Set medium size for error panel**
////        panel.preferredSize = Dimension(1200, 600)
//        return panel
//    }
//
//    // **Helper Method: Create Section Title Label**
//    private fun createSectionLabel(title: String): JLabel {
//        val label = JLabel(title)
//        label.font = Font("Arial", Font.BOLD, 16 )
//        return label
//    }
//}
//
////// **Custom Cell Renderer for Multi-Line Text**
//class MultiLineTableCellRenderer : JTextArea(), TableCellRenderer {
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


package com.example.sidebarplugin.GitKB.GitKBResponse

import com.intellij.ui.components.JBScrollPane
import kotlinx.serialization.json.*
import java.awt.*
import javax.swing.*

object JsonExplain {
    fun extractExplain(response: String): JPanel {
        println("Raw API Response (JsonExplainCode): $response")

        if (!response.trim().startsWith("{")) {
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val mainPanel = JPanel()
            mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
            mainPanel.background = DARK_BG

            // ============ QUALITY Section (including Remarks) ============
            mainPanel.add(createSectionLabel("Summary"))

            // Show the overview for Quality
            val qualityArray = extractArray(jsonElement["quality"])
            qualityArray.forEach { value ->
                mainPanel.add(createCard("Quality", value))  // Show only the overview for Quality
                mainPanel.add(Box.createVerticalStrut(10))
            }

            // Include Remarks under the Quality section
            val remarksArray = extractArray(jsonElement["remarks"])
            remarksArray.forEach { value ->
                mainPanel.add(createCard("Remarks", value))  // Show only the overview for Remarks
                mainPanel.add(Box.createVerticalStrut(10))
            }

            // ============ EXPLANATION Section ============
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(createSectionLabel("Explanation"))
            val explanationArray = jsonElement["explanation"]?.jsonArray ?: JsonArray(emptyList())
            for (item in explanationArray) {
                val obj = item.jsonObject
                val overview = obj["overview"]?.jsonPrimitive?.content ?: "N/A"
                val detailed = obj["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"

                // Create a card with the same style as Quality
                val card = JPanel()
                card.layout = BoxLayout(card, BoxLayout.Y_AXIS)
                card.border = BorderFactory.createLineBorder(LIGHT_TEXT)  // Border similar to Quality
                card.background = DARK_PANEL  // Background similar to Quality

                // Adding Overview and Detailed Explanation to the card
                card.add(createLabeledTextArea("Overview", overview))
                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Detailed Explanation", detailed, height = 200))

                mainPanel.add(card)
                mainPanel.add(Box.createVerticalStrut(15))
            }

            val outerScroll = JBScrollPane(mainPanel).apply {
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(900, 800)
                background = DARK_BG
            }

            return JPanel(BorderLayout()).apply {
                background = DARK_BG
                add(outerScroll, BorderLayout.CENTER)
            }

        } catch (e: Exception) {
            errorPanel("Invalid JSON response: ${e.message}")
        }
    }

    private fun extractArray(element: JsonElement?): List<String> {
        return when {
            element == null -> emptyList()
            element is JsonArray -> element.mapNotNull { it.jsonPrimitive.contentOrNull }
            element is JsonPrimitive -> listOf(element.content)
            else -> emptyList()
        }
    }

    private fun createSectionLabel(title: String): JLabel {
        return JLabel(title).apply {
            font = Font("Arial", Font.BOLD, 18)
            foreground = LIGHT_TEXT
            alignmentX = Component.LEFT_ALIGNMENT
            border = BorderFactory.createEmptyBorder(10, 10, 5, 10)
        }
    }

    private fun createCard(label: String?, content: String): JPanel {
        val card = JPanel()
        card.layout = BoxLayout(card, BoxLayout.Y_AXIS)
        card.border = BorderFactory.createLineBorder(LIGHT_TEXT)
        card.background = DARK_PANEL

        card.add(createLabeledTextArea(label, content))
        return card
    }

    private fun createLabeledTextArea(label: String?, content: String, height: Int = 120): JPanel {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        panel.alignmentX = Component.LEFT_ALIGNMENT
        panel.background = DARK_PANEL

        if (!label.isNullOrBlank()) {
            val labelComponent = JLabel(label).apply {
                font = Font("Arial", Font.BOLD, 14)
                foreground = LIGHT_TEXT
            }
            panel.add(labelComponent)
            panel.add(Box.createVerticalStrut(5))
        }

        val textArea = JTextArea(content).apply {
            lineWrap = true
            wrapStyleWord = true
            isEditable = false
            font = Font("Arial", Font.PLAIN, 13)
            background = DARK_TEXTAREA
            foreground = LIGHT_TEXT
            border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
        }

        val scrollPane = JBScrollPane(textArea).apply {
            preferredSize = Dimension(850, height)  // Use dynamic height here
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            background = DARK_PANEL
        }

        panel.add(scrollPane)
        return panel
    }


    private fun errorPanel(message: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.background = DARK_BG
        val label = JLabel(message)
        label.foreground = Color.RED
        panel.add(label, BorderLayout.CENTER)
        panel.preferredSize = Dimension(800, 200)
        return panel
    }

    // Colors
    private val DARK_BG = Color(60, 63, 65)
    private val DARK_PANEL = Color(75, 78, 80)
    private val DARK_TEXTAREA = Color(50, 53, 55)
    private val LIGHT_TEXT = Color(187, 187, 187)
}

