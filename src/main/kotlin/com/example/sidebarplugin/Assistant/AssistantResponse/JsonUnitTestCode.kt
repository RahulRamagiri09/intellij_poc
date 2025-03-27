package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.serialization.json.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellRenderer
import java.awt.*

object JsonUnitTestCode {
    fun extractUnitTestCode(response: String): JPanel {
        println("Raw API Response (JsonUnitTestCode): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Vertical layout for sections

            // **Details Section**
            panel.add(createSectionLabel("Details:"))
            panel.add(createTable(arrayOf("Description"), listOf(
                arrayOf(jsonElement["details"]?.jsonPrimitive?.content ?: "N/A")
            )))
            panel.add(Box.createVerticalStrut(15))


            // **Unit Tests Section**
            panel.add(createSectionLabel("Unit Test Cases:"))
            val unitTestData = jsonElement["unitTests"]?.jsonArray?.map { test ->
                val testObject = test.jsonObject
                arrayOf<Any>(
                    "<html><pre>${testObject["testCase"]?.jsonPrimitive?.content?.replace("\n", "<br>") ?: "N/A"}</pre></html>",
                    testObject["explanation"]?.jsonPrimitive?.content ?: "N/A",
                    testObject["importance"]?.jsonPrimitive?.int ?: 0 as Any, // Explicitly cast to Any
                    testObject["severity"]?.jsonPrimitive?.content ?: "N/A"

                )
            } ?: emptyList()

            panel.add(createTable(arrayOf("Test Case", "Explanation", "Importance", "Severity"), unitTestData))
            panel.add(Box.createVerticalStrut(15))

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

    // **Helper Method: Create Section Labels**
    private fun createSectionLabel(text: String): JPanel {
        val label = JLabel(text)
        label.font = Font("Arial", Font.BOLD, 14)

        val wrapper = JPanel()
        wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS)
        wrapper.add(Box.createVerticalStrut(10)) // Top spacing
        wrapper.add(label)
        wrapper.add(Box.createVerticalStrut(5)) // Bottom spacing

        return wrapper
    }

    // **Helper Method: Create Table with Headers & Multi-Line Renderer**
    private fun createTable(columnNames: Array<String>, data: List<Array<Any>>): JPanel {
        val model = DefaultTableModel(columnNames, 0)
        data.forEach { model.addRow(it) }

        val table = JBTable(model)
        val renderer = MultiLineTableCellRendererrr()

        for (i in 0 until table.columnCount) {
            table.columnModel.getColumn(i).cellRenderer = renderer
        }

//        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.autoResizeMode = JTable.AUTO_RESIZE_OFF // 🔹 Prevent auto shrinking
        if (table.columnCount >= 4) {  // 🔹 Ensure columns exist before setting width
            table.columnModel.getColumn(0).preferredWidth = 500  // Test Case
            table.columnModel.getColumn(1).preferredWidth = 500  // Explanation
            table.columnModel.getColumn(2).preferredWidth = 80  // Importance
            table.columnModel.getColumn(3).preferredWidth = 80  // Severity
        }
        table.rowHeight = 150 // Adjust row height for better readability
        table.tableHeader.reorderingAllowed = false // Prevent column reordering

        val tablePanel = JPanel(BorderLayout())
        tablePanel.add(table.tableHeader, BorderLayout.NORTH) // **Ensure header is visible**
        tablePanel.add(table, BorderLayout.CENTER)

        return tablePanel
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
class MultiLineTableCellRendererrr : JTextArea(), TableCellRenderer {
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




