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
//            panel.add(createTable(arrayOf("Quality", "Remarks", "Severity"), summaryData))
//            panel.add(Box.createVerticalStrut(15)) // Spacing
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
//                            issueObj["severity"]?.jsonPrimitive?.content ?: "N/A"
//                        )
//                    }
//
//                    panel.add(createTable(arrayOf("Issue", "Explanation", "Fix", "Severity"), issueData))
//                }
//            }
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
//    // **Helper Method: Create Table with Multi-Line Renderer**
//    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JPanel {
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
//        table.autoResizeMode = JTable.AUTO_RESIZE_OFF // Prevent auto shrinking
//
//        // 🔹 Set column widths for better readability
//        if (table.columnCount >= 5) {
//            table.columnModel.getColumn(0).preferredWidth = 500  // Issue
//            table.columnModel.getColumn(1).preferredWidth = 300  // Explanation
//            table.columnModel.getColumn(2).preferredWidth = 500  // Fix
//            table.columnModel.getColumn(4).preferredWidth = 80   // Severity
//        }
//
//        table.rowHeight = 250 // Adjust row height for better readability
//        table.tableHeader.reorderingAllowed = false // Prevent column reordering
//
//        val tablePanel = JPanel(BorderLayout())
//        tablePanel.add(table.tableHeader, BorderLayout.NORTH) // Ensure header is visible
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
//        panel.preferredSize = Dimension(800, 200) // Set medium size for error panel
//        return panel
//    }
//
//    // **Custom Cell Renderer for Multi-Line Text**
//    class MultiLineTableCellRenderer : JTextArea(), TableCellRenderer {
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
//    private fun createSectionLabel(title: String): JPanel {
//        val label = JLabel(title)
//        label.font = Font("Arial", Font.BOLD, 16)
//
//        val wrapper = JPanel()
//        wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS)
//        wrapper.add(Box.createVerticalStrut(10)) // Top spacing
//        wrapper.add(label)
//        wrapper.add(Box.createVerticalStrut(5)) // Bottom spacing
//
//        return wrapper
//    }
//}


//
//package com.example.sidebarplugin.Assistant.AssistantResponse;
//
//import com.intellij.ui.components.JBScrollPane;
//import com.intellij.ui.table.JBTable;
//import kotlinx.serialization.json.*;
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import javax.swing.table.TableCellRenderer;
//
//object JsonOverallReview {
//    fun extractOverallReview(response: String): JPanel {
//        println("Raw API Response (JsonOverallReview): $response"); // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return errorPanel("Error: Response is not valid JSON");
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject;
//            val panel = JPanel();
//            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS); // Set vertical layout
//
//            // **Summary Table**
//            panel.add(createSectionLabel("Summary"));
//            val summaryData = listOf(
//                arrayOf(
//                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A",
//                    jsonElement["overallSeverity"]?.jsonPrimitive?.content ?: "N/A"
//                )
//            );
//            panel.add(createTable(arrayOf("Quality", "Remarks", "Severity"), summaryData));
//            panel.add(Box.createVerticalStrut(15)); // Spacing
//
//            // **Issue Tables for Different Categories**
//            val issues = jsonElement["issues"]?.jsonObject;
//            val categoryOrder = listOf("Quality", "Performance", "Security", "Organization Standards", "CK Metrics", "Syntax");
//
//            categoryOrder.forEach { category ->
//                val issuesArray = issues?.get(category)?.jsonArray;
//                if (issuesArray != null && issuesArray.isNotEmpty()) {
//                    panel.add(Box.createVerticalStrut(20)); // Add spacing
//                    panel.add(createSectionLabel(category)); // Section Title
//
//                    val issueData = issuesArray.map { issue ->
//                        val issueObj = issue.jsonObject;
//                        arrayOf(
//                            issueObj["identification"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["explanation"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["fix"]?.jsonPrimitive?.content ?: "N/A",
//                            issueObj["severity"]?.jsonPrimitive?.content ?: "N/A"
//                        );
//                    };
//
//                    panel.add(createTable(arrayOf("Issue", "Explanation", "Fix", "Severity"), issueData));
//                }
//            }
//
//            // **Wrap the ENTIRE panel inside a scroll pane**
//            val scrollPane = JBScrollPane(panel);
//            scrollPane.preferredSize = Dimension(1200, 600);
//
//            val containerPanel = JPanel(BorderLayout());
//            containerPanel.preferredSize = Dimension(1200, 600);
//            containerPanel.add(scrollPane, BorderLayout.CENTER);
//
//            containerPanel;
//        } catch (e: Exception) {
//            errorPanel("Invalid JSON response: ${e.message}");
//        }
//    }
//
//    // **Helper Method: Create Table with Multi-Line Renderer & Row Scrollbars**
//    private fun createTable(columnNames: Array<String>, data: List<Array<String>>): JPanel {
//        val model = DefaultTableModel(columnNames, 0);
//        data.forEach { model.addRow(it); };
//
//        val table = JBTable(model);
//        val renderer = MultiLineTableCellRenderer();
//
//        for (i in 0 until table.columnCount) {
//            table.columnModel.getColumn(i).cellRenderer = renderer;
//        }
//
//        table.autoResizeMode = JTable.AUTO_RESIZE_OFF; // Prevent auto shrinking
//
//        // 🔹 Set column widths for better readability
//        if (table.columnCount >= 4) {
//            table.columnModel.getColumn(0).preferredWidth = 500;  // Increased width for "Quality"
//            table.columnModel.getColumn(1).preferredWidth = 500;  // Increased width for "Remarks"
//            table.columnModel.getColumn(2).preferredWidth = 250;  // Width for "Severity"
//            table.columnModel.getColumn(3).preferredWidth = 80;   // Width for "Severity"
//        }
//
//        table.rowHeight = 300; // Increased row height for better readability
//        table.tableHeader.reorderingAllowed = false; // Prevent column reordering
//
//        val scrollPane = JBScrollPane(table);
//        scrollPane.preferredSize = Dimension(1100, 250);
//        scrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
//
//        val tablePanel = JPanel(BorderLayout());
//        tablePanel.add(table.tableHeader, BorderLayout.NORTH); // Ensure header is visible
//        tablePanel.add(scrollPane, BorderLayout.CENTER);
//
//        return tablePanel;
//    }
//
//    // **Utility Method: Error Panel**
//    private fun errorPanel(message: String): JPanel {
//        val panel = JPanel(BorderLayout());
//        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))));
//        panel.preferredSize = Dimension(800, 200); // Set medium size for error panel
//        return panel;
//    }
//
//    // **Custom Cell Renderer for Multi-Line Text with Scroll Support**
//    class MultiLineTableCellRenderer : JScrollPane(), TableCellRenderer {
//        private val textArea = JTextArea();
//
//        init {
//            textArea.lineWrap = true;
//            textArea.wrapStyleWord = true;
//            textArea.isOpaque = true;
//            setViewportView(textArea);
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
//            textArea.text = value?.toString() ?: "";
//            return this;
//        }
//    }
//
//    // **Helper Method: Section Title Label**
//    private fun createSectionLabel(title: String): JPanel {
//        val label = JLabel(title);
//        label.font = Font("Arial", Font.BOLD, 16);
//
//        val wrapper = JPanel();
//        wrapper.layout = BoxLayout(wrapper, BoxLayout.Y_AXIS);
//        wrapper.add(Box.createVerticalStrut(10)); // Top spacing
//        wrapper.add(label);
//        wrapper.add(Box.createVerticalStrut(5)); // Bottom spacing
//
//        return wrapper;
//    }
//}


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

    // **Helper Method: Create Table with Multi-Line Renderer & Row Scrollbars**
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
        if (table.columnCount >= 4) {
            table.columnModel.getColumn(0).preferredWidth = 400  // Issue
            table.columnModel.getColumn(1).preferredWidth = 300  // Explanation
            table.columnModel.getColumn(2).preferredWidth = 400  // Fix
            table.columnModel.getColumn(3).preferredWidth = 80   // Severity
        }

        // Dynamically calculate row height based on content
        for (row in 0 until table.rowCount) {
            var maxHeight = table.rowHeight
            for (col in 0 until table.columnCount) {
                val cellRenderer = table.getCellRenderer(row, col)
                val cellComponent = cellRenderer.getTableCellRendererComponent(
                    table, table.getValueAt(row, col), false, false, row, col
                )
                maxHeight = maxHeight.coerceAtLeast(cellComponent.preferredSize.height + 20) // Add padding
            }
            table.setRowHeight(row, maxHeight)
        }

        val scrollPane = JBScrollPane(table)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        val tablePanel = JPanel(BorderLayout())
        tablePanel.add(scrollPane, BorderLayout.CENTER)

        return tablePanel
    }

    // **Utility Method: Error Panel**
    private fun errorPanel(message: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.add(JBScrollPane(JTable(arrayOf(arrayOf(message)), arrayOf("Error"))))
        panel.preferredSize = Dimension(800, 200) // Set medium size for error panel
        return panel
    }

    // **Custom Cell Renderer for Multi-Line Text with Scroll Support**
    class MultiLineTableCellRenderer : JScrollPane(), TableCellRenderer {
        private val textArea = JTextArea()

        init {
            textArea.lineWrap = true
            textArea.wrapStyleWord = true
            textArea.isOpaque = true
            textArea.setRows(5)
            textArea.setColumns(20)
            verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            setViewportView(textArea)
        }

        override fun getTableCellRendererComponent(
            table: JTable,
            value: Any?,
            isSelected: Boolean,
            hasFocus: Boolean,
            row: Int,
            column: Int
        ): Component {
            textArea.text = value?.toString() ?: ""
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


