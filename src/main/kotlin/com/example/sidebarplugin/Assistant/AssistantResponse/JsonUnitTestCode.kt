//package com.example.sidebarplugin.Assistant.AssistantResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import javax.swing.table.TableCellRenderer
//import java.awt.*
//
//object JsonUnitTestCode {
//    fun extractUnitTestCode(response: String): JPanel {
//        println("Raw API Response (JsonUnitTestCode): $response") // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return errorPanel("Error: Response is not valid JSON")
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            val panel = JPanel()
//            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Vertical layout for sections
//
//            // **Details Section**
//            panel.add(createSectionLabel("Details:"))
//            panel.add(createTable(arrayOf("Description"), listOf(
//                arrayOf(jsonElement["details"]?.jsonPrimitive?.content ?: "N/A")
//            )))
//            panel.add(Box.createVerticalStrut(20))
//
//
//            // **Unit Tests Section**
//            panel.add(createSectionLabel("Unit Test Cases:"))
//            val unitTestData = jsonElement["unitTests"]?.jsonArray?.map { test ->
//                val testObject = test.jsonObject
//                arrayOf<Any>(
//                    testObject["testCase"]?.jsonPrimitive?.content ?: "N/A",
//                    testObject["explanation"]?.jsonPrimitive?.content ?: "N/A",
//                    testObject["importance"]?.jsonPrimitive?.int ?: 0 as Any,
//                    testObject["severity"]?.jsonPrimitive?.content ?: "N/A"
//
//                )
//            } ?: emptyList()
//
//            panel.add(createTable(arrayOf("Test Case", "Explanation", "Importance", "Severity"), unitTestData))
//            panel.add(Box.createVerticalStrut(20))
//
//            // **Wrap the ENTIRE panel inside a scroll pane**
//            val scrollPane = JBScrollPane(panel)
//            scrollPane.preferredSize = Dimension(1200, 600)
//
//            val containerPanel = JPanel(BorderLayout())
//            containerPanel.preferredSize = Dimension(1200, 600)
//            containerPanel.add(scrollPane, BorderLayout.CENTER)
//
//            containerPanel
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }
//
//    // **Helper Method: Create Section Labels**
//    private fun createSectionLabel(text: String): JPanel {
//        val label = JLabel(text)
//        label.font = Font("Arial", Font.BOLD, 14)
//
//        val wrapper = JPanel()
//        wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS)
//        wrapper.add(Box.createVerticalStrut(10)) // Top spacing
//        wrapper.add(label)
//        wrapper.add(Box.createVerticalStrut(5)) // Bottom spacing
//
//        return wrapper
//    }
//
//    private fun createTable(columnNames: Array<String>, data: List<Array<Any>>): JPanel {
//        val model = DefaultTableModel(columnNames, 0)
//        data.forEach { model.addRow(it) }
//
//        val table = JBTable(model)
//        val renderer = MultiLineTableCellRendererrr()
//
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer
//        }
//
//        // Set custom header style
//        val headerFont = Font("Arial", Font.BOLD, 14)
//        val headerRenderer = object : JLabel(), TableCellRenderer {
//            init {
//                font = headerFont
//                horizontalAlignment = JLabel.CENTER
//                isOpaque = true
//                background = Color(0x07, 0x43, 0x9C) // Custom blue
//                foreground = Color.WHITE
//            }
//
//            override fun getTableCellRendererComponent(
//                table: JTable,
//                value: Any?,
//                isSelected: Boolean,
//                hasFocus: Boolean,
//                row: Int,
//                column: Int
//            ): Component {
//                text = value?.toString() ?: ""
//                return this
//            }
//        }
//
//        table.tableHeader.defaultRenderer = headerRenderer
//
//        // Table layout and appearance tweaks
//        table.autoResizeMode = JTable.AUTO_RESIZE_OFF
//        if (table.columnCount >= 4) {
//            table.columnModel.getColumn(0).preferredWidth = 500
//            table.columnModel.getColumn(1).preferredWidth = 500
//            table.columnModel.getColumn(2).preferredWidth = 80
//            table.columnModel.getColumn(3).preferredWidth = 80
//        }
//
//        table.rowHeight = 150
//        table.tableHeader.reorderingAllowed = false
//
//        val tablePanel = JPanel(BorderLayout())
//        tablePanel.add(table.tableHeader, BorderLayout.NORTH)
//        tablePanel.add(table, BorderLayout.CENTER)
//
//        return tablePanel
//    }
//
//
//    // **Utility Method: Error Panel**
//    private fun errorPanel(message: String): JPanel {
//        val panel = JPanel(BorderLayout())
//        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
//        panel.preferredSize = Dimension(800, 200) // **Set medium size for error panel**
//        return panel
//    }
//}
//
//// **Custom Cell Renderer for Multi-Line Text**
//class MultiLineTableCellRendererrr : JTextArea(), TableCellRenderer {
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
////        background = if (isSelected) Color(0xD9E1F2) else Color.WHITE
////        foreground = Color.WHITE
//        return this
//    }
//}
//
//
//
//

package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import kotlinx.serialization.json.*
import java.awt.*
import javax.swing.*

object JsonUnitTestCode {
    fun extractUnitTestCode(response: String): JPanel {
        println("Raw API Response (JsonUnitTestCode): $response")

        if (!response.trim().startsWith("{")) {
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val mainPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                background = DARK_BG
            }

            // ======= Details Section =======
            mainPanel.add(createSectionLabel("Details"))

            val details = jsonElement["details"]?.jsonPrimitive?.content ?: "N/A"
            mainPanel.add(createCard("Description", details))
            mainPanel.add(Box.createVerticalStrut(20))

            // ======= Unit Test Cases Section =======
            mainPanel.add(createSectionLabel("Unit Test Cases"))

            val unitTestData = jsonElement["unitTests"]?.jsonArray ?: JsonArray(emptyList())
                // without Index in the TestCase
//            for (test in unitTestData) {
//                val testObject = test.jsonObject
//                val testCase = testObject["testCase"]?.jsonPrimitive?.content ?: "N/A"
//                val explanation = testObject["explanation"]?.jsonPrimitive?.content ?: "N/A"
//                val importance = testObject["importance"]?.jsonPrimitive?.intOrNull?.toString() ?: "0"
//                val severity = testObject["severity"]?.jsonPrimitive?.content ?: "N/A"
//
//                val card = JPanel().apply {
//                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
//                    border = BorderFactory.createLineBorder(LIGHT_TEXT)
//                    background = DARK_PANEL
//                }
//
//                card.add(createLabeledTextArea("Test Case", testCase, height = 150))
//                card.add(Box.createVerticalStrut(10))
//                card.add(createLabeledTextArea("Explanation", explanation))
//                card.add(Box.createVerticalStrut(10))
//                card.add(createLabeledTextArea("Importance", importance, height = 40))
//                card.add(Box.createVerticalStrut(10))
//                card.add(createLabeledTextArea("Severity", severity, height = 40))
//
//                mainPanel.add(card)
//                mainPanel.add(Box.createVerticalStrut(15))
//            }
            // Inside the loop where unit tests are processed
            var testIndex = 1 // Initialize an index counter

            for (test in unitTestData) {
                val testObject = test.jsonObject
                val testCase = testObject["testCase"]?.jsonPrimitive?.content ?: "N/A"
                val explanation = testObject["explanation"]?.jsonPrimitive?.content ?: "N/A"
                val importance = testObject["importance"]?.jsonPrimitive?.intOrNull?.toString() ?: "0"
                val severity = testObject["severity"]?.jsonPrimitive?.content ?: "N/A"

                val card = JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    border = BorderFactory.createLineBorder(LIGHT_TEXT)
                    background = DARK_PANEL
                }

                // Add index to the Test Case heading
                card.add(createLabeledTextArea("${testIndex}. Test Case", testCase, height = 150)) // Added index to the label
                testIndex++ // Increment the index for the next test case

                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Explanation", explanation))
                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Importance", importance, height = 40))
                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Severity", severity, height = 40))

                mainPanel.add(card)
                mainPanel.add(Box.createVerticalStrut(15))
            }


            val outerScroll = JBScrollPane(mainPanel).apply {
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(1200, 800)
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
        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            background = DARK_PANEL
            border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        }

        if (!label.isNullOrBlank()) {
            val labelComponent = JLabel(label).apply {
                font = Font("Arial", Font.BOLD, 14)
                foreground = LIGHT_TEXT
                alignmentX = Component.LEFT_ALIGNMENT
                border = BorderFactory.createEmptyBorder(0, 5, 0, 0)
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
            preferredSize = Dimension(1100, height)
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            background = DARK_PANEL
            border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
            viewportBorder = null
            alignmentX = Component.LEFT_ALIGNMENT
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
