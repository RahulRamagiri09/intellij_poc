//package com.example.sidebarplugin.AssistantResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import java.awt.*
//import javax.swing.table.TableCellRenderer
//
//
//object JsonExplainCode {
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
//            panel.preferredSize = Dimension(900, 600) // **Set medium size for panel**
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
//        val renderer = MultiLineTableCellRenderer()
//
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer
//        }
//
//        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
//        table.rowHeight = 40 // Adjust row height for better readability
//
//        return table
//    }
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

//
//package com.example.sidebarplugin.AssistantResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import javax.swing.table.TableCellRenderer
//import java.awt.*
//
//object JsonExplainCode {
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
//            panel.add(createSectionLabel("Summary:"))
//            val summaryData = listOf(
//                arrayOf(
//                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            )
//            panel.add(createTable(arrayOf("Quality", "Remarks"), summaryData))
//
//            // **Explanation Section (NO SCROLLER)**
//            panel.add(createSectionLabel("Explanation:"))
//            val explanationData = jsonElement["explanation"]?.jsonArray?.map { explanation ->
//                val explanationObject = explanation.jsonObject
//                arrayOf(
//                    explanationObject["overview"]?.jsonPrimitive?.content ?: "N/A",
//                    explanationObject["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            } ?: emptyList()
//            panel.add(createTable(arrayOf("Overview", "Detailed Explanation"), explanationData))
//
//            // **Key Components Section**
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
//            panel.add(createTable(arrayOf("Name", "Description"), keyComponentsData))
//
//            // **Logic Flow Section**
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
//            panel.add(createTable(arrayOf("Step", "Purpose"), logicFlowData))
//
//            // **Algorithms Section**
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
//            panel.add(createTable(arrayOf("Name", "Description"), algorithmsData))
//
//            // **Wrap the ENTIRE panel inside a scroll pane**
//            val scrollPane = JBScrollPane(panel)
//            scrollPane.preferredSize = Dimension(900, 600)
//
//            val containerPanel = JPanel(BorderLayout())
//            containerPanel.add(scrollPane, BorderLayout.CENTER)
//
//            containerPanel
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }
//
//    // **Helper Method: Create Section Labels**
//    private fun createSectionLabel(text: String): JLabel {
//        val label = JLabel(text)
//        label.font = Font("Arial", Font.BOLD, 14)
//        val wrapper = JPanel()
//        wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS)
//        wrapper.add(Box.createVerticalStrut(20))
//        wrapper.add(label)
//        wrapper.add(Box.createVerticalStrut(10))
//        return label
//    }
//
//    // **Helper Method: Create Table with Multi-Line Renderer**
//    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JBTable {
//        val model = DefaultTableModel(columnNames, 0)
//        data.forEach { model.addRow(it) }
//
//        val table = JBTable(model)
//        val renderer = MultiLineTableCellRenderer()
//
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer
//        }
//
//        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
//        table.rowHeight = 40 // Adjust row height for better readability
//
//        return table
//    }
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


package com.example.sidebarplugin.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import kotlinx.serialization.json.*
import javax.swing.*
import javax.swing.table.DefaultTableModel
import javax.swing.table.TableCellRenderer
import java.awt.*

object JsonExplainCode {
    fun extractExplainCode(response: String): JPanel {
        println("Raw API Response (JsonExplainCode): $response") // Debugging log

        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val panel = JPanel()
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS) // Vertical layout for sections

            // **Summary Section**
            panel.add(createSectionLabel("Summary:"))
            panel.add(createTable(arrayOf("Quality", "Remarks"), listOf(
                arrayOf(
                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
                )
            )))
            panel.add(Box.createVerticalStrut(15)) // **Spacing between tables**

            // **Explanation Section**
            panel.add(createSectionLabel("Explanation:"))
            val explanationData = jsonElement["explanation"]?.jsonArray?.map { explanation ->
                val explanationObject = explanation.jsonObject
                arrayOf(
                    explanationObject["overview"]?.jsonPrimitive?.content ?: "N/A",
                    explanationObject["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
                )
            } ?: emptyList()
            panel.add(createTable(arrayOf("Overview", "Detailed Explanation"), explanationData))
            panel.add(Box.createVerticalStrut(15))

            // **Key Components Section**
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
            panel.add(createTable(arrayOf("Name", "Description"), keyComponentsData))
            panel.add(Box.createVerticalStrut(15))

            // **Logic Flow Section**
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
            panel.add(createTable(arrayOf("Step", "Purpose"), logicFlowData))
            panel.add(Box.createVerticalStrut(15))

            // **Algorithms Section**
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
            panel.add(createTable(arrayOf("Name", "Description"), algorithmsData))
            panel.add(Box.createVerticalStrut(15))

            // **Wrap the ENTIRE panel inside a scroll pane**
            val scrollPane = JBScrollPane(panel)
            scrollPane.preferredSize = Dimension(900, 600)

            val containerPanel = JPanel(BorderLayout())
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
    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JPanel {
        val model = DefaultTableModel(columnNames, 0)
        data.forEach { model.addRow(it) }

        val table = JBTable(model)
        val renderer = MultiLineTableCellRenderer()

        for (i in 0 until table.columnCount) {
            table.columnModel.getColumn(i).cellRenderer = renderer
        }

        table.autoResizeMode = JTable.AUTO_RESIZE_ALL_COLUMNS
        table.rowHeight = 40 // Adjust row height for better readability
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
//
//package com.example.sidebarplugin.AssistantResponse
//
//import com.intellij.ui.components.JBScrollPane
//import com.intellij.ui.table.JBTable
//import kotlinx.serialization.json.*
//import javax.swing.*
//import javax.swing.table.DefaultTableModel
//import java.awt.*
//
//object JsonExplainCode {
//    fun extractExplainCode(response: String): JPanel {
//        println("Raw API Response (JsonExplainCode): $response") // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return errorPanel("Error: Response is not valid JSON")
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//
//            // **Main panel**
//            val mainPanel = JPanel()
//            mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS) // Vertical layout
//
//            // **Scrollable content panel**
//            val contentPanel = JPanel()
//            contentPanel.layout = BoxLayout(contentPanel, BoxLayout.Y_AXIS)
//
//            // **Summary Section**
//            contentPanel.add(sectionLabel("Summary"))
//            contentPanel.add(Box.createVerticalStrut(10))
//
//            val qualityRemarksModel = DefaultTableModel(arrayOf("Quality", "Remarks"), 0)
//            qualityRemarksModel.addRow(
//                arrayOf(
//                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            )
//            contentPanel.add(tableWithSpacing(qualityRemarksModel))
//
//            // **Explanation Section**
//            contentPanel.add(sectionLabel("Explanation"))
//            contentPanel.add(Box.createVerticalStrut(10))
//
//            val explanationModel = DefaultTableModel(arrayOf("Overview", "Detailed Explanation"), 0)
//            jsonElement["explanation"]?.jsonArray?.forEach { explanation ->
//                val explanationObject = explanation.jsonObject
//                explanationModel.addRow(
//                    arrayOf(
//                        explanationObject["overview"]?.jsonPrimitive?.content ?: "N/A",
//                        explanationObject["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
//                    )
//                )
//            }
//            contentPanel.add(tableWithSpacing(explanationModel))
//
//            // **Key Components Section**
//            contentPanel.add(sectionLabel("Key Components"))
//            contentPanel.add(Box.createVerticalStrut(10))
//
//            val keyComponentsModel = DefaultTableModel(arrayOf("Name", "Description"), 0)
//            jsonElement["explanation"]?.jsonArray?.forEach { explanation ->
//                explanation.jsonObject["keyComponents"]?.jsonArray?.forEach { component ->
//                    val compObj = component.jsonObject
//                    keyComponentsModel.addRow(
//                        arrayOf(
//                            compObj["name"]?.jsonPrimitive?.content ?: "N/A",
//                            compObj["description"]?.jsonPrimitive?.content ?: "N/A"
//                        )
//                    )
//                }
//            }
//            contentPanel.add(tableWithSpacing(keyComponentsModel))
//
//            // **Logic Flow Section**
//            contentPanel.add(sectionLabel("Logic Flow"))
//            contentPanel.add(Box.createVerticalStrut(10))
//
//            val logicFlowModel = DefaultTableModel(arrayOf("Step", "Purpose"), 0)
//            jsonElement["explanation"]?.jsonArray?.forEach { explanation ->
//                explanation.jsonObject["logicFlow"]?.jsonArray?.forEach { step ->
//                    val stepObj = step.jsonObject
//                    logicFlowModel.addRow(
//                        arrayOf(
//                            stepObj["step"]?.jsonPrimitive?.content ?: "N/A",
//                            stepObj["purpose"]?.jsonPrimitive?.content ?: "N/A"
//                        )
//                    )
//                }
//            }
//            contentPanel.add(tableWithSpacing(logicFlowModel))
//
//            // **Algorithms Section**
//            contentPanel.add(sectionLabel("Algorithms"))
//            contentPanel.add(Box.createVerticalStrut(10))
//
//            val algorithmsModel = DefaultTableModel(arrayOf("Name", "Description"), 0)
//            jsonElement["explanation"]?.jsonArray?.forEach { explanation ->
//                explanation.jsonObject["algorithms"]?.jsonArray?.forEach { algo ->
//                    val algoObj = algo.jsonObject
//                    algorithmsModel.addRow(
//                        arrayOf(
//                            algoObj["name"]?.jsonPrimitive?.content ?: "N/A",
//                            algoObj["description"]?.jsonPrimitive?.content ?: "N/A"
//                        )
//                    )
//                }
//            }
//            contentPanel.add(tableWithSpacing(algorithmsModel))
//
//            // **Wrap content panel inside a JScrollPane**
//            val scrollPane = JBScrollPane(contentPanel)
//            scrollPane.preferredSize = Dimension(900, 600)
//
//            // Add scroll pane to the main panel
//            mainPanel.add(scrollPane)
//            mainPanel
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}")
//        }
//    }
//
//    // **Creates a section label with bold text**
//    private fun sectionLabel(text: String): JLabel {
//        val label = JLabel(text + ":")
//        label.font = Font("Arial", Font.BOLD, 14)
//        return label
//    }
//
//    // **Creates a table with spacing below it**
//    private fun tableWithSpacing(model: DefaultTableModel): JPanel {
//        val panel = JPanel(BorderLayout())
//        panel.add(JBScrollPane(JBTable(model)), BorderLayout.CENTER)
//        panel.add(Box.createVerticalStrut(15), BorderLayout.SOUTH) // Add spacing below the table
//        return panel
//    }
//
//    // **Utility method to show errors in UI**
//    private fun errorPanel(message: String): JPanel {
//        val panel = JPanel(BorderLayout())
//        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
//        panel.preferredSize = Dimension(800, 200)
//        return panel
//    }
//}
