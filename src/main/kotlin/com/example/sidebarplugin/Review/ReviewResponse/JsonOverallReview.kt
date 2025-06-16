package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import kotlinx.serialization.json.*
import java.awt.*
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import java.io.File
import java.awt.Font as AwtFont

object JsonOverallReview {
    fun extractOverallReview(response: String): JPanel {
        println("Raw API Response (JsonOverallReview): $response")

        if (!response.trim().startsWith("{")) {
            return errorPanel("Error: Response is not valid JSON")
        }

        return try {
            val jsonElement = Json.parseToJsonElement(response).jsonObject
            val mainPanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                background = DARK_BG
            }

            // ======= Summary Section =======
            mainPanel.add(createSectionLabel("Summary"))
            val summaryData = listOf(
                arrayOf(
                    jsonElement["quality"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["overallSeverity"]?.jsonPrimitive?.content ?: "N/A",
                    jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A",
                )
            )
            mainPanel.add(createCard("Quality", summaryData[0][0], height = 40))
            mainPanel.add(createCard("Severity", summaryData[0][1], height = 40))
            mainPanel.add(createCard("Remarks", summaryData[0][2], height = 180))
            mainPanel.add(Box.createVerticalStrut(15))

            // ======= Issues =======
//            mainPanel.add(createSectionLabel("Issues"))
            val issues = jsonElement["issues"]?.jsonObject
            val categoryOrder = listOf("Quality", "Performance", "Security", "Organization Standards", "CK Metrics", "Syntax", "Cloud Violations")

            categoryOrder.forEach { category ->
                val issuesArray = issues?.get(category)?.jsonArray
                if (issuesArray != null && issuesArray.isNotEmpty()) {
                    mainPanel.add(Box.createVerticalStrut(20))
                    mainPanel.add(createSectionLabel(category))

                    issuesArray.forEach { issue ->
                        val issueObj = issue.jsonObject
                        val issueData = arrayOf(
                            issueObj["identification"]?.jsonPrimitive?.content ?: "N/A",
                            issueObj["explanation"]?.jsonPrimitive?.content ?: "N/A",
                            issueObj["fix"]?.jsonPrimitive?.content ?: "N/A",
                            issueObj["severity"]?.jsonPrimitive?.content ?: "N/A"
                        )
                        mainPanel.add(createCard("Issue", issueData[0]))
                        mainPanel.add(createCard("Explanation", issueData[1]))
                        mainPanel.add(createCard("Fix", issueData[2]))

                        // Add violation type and policy reference for Cloud Violations right after Fix
                        if (category == "Cloud Violations") {
                            val violationType = issueObj["violationType"]?.jsonPrimitive?.content ?: "N/A"
                            val policyReference = issueObj["policyReference"]?.jsonPrimitive?.content ?: "N/A"
                            mainPanel.add(createCard("Violation Type", violationType, height = 40))
                            mainPanel.add(createCard("Policy Reference", policyReference, height = 40))
                        }

                        mainPanel.add(createCard("Severity", issueData[3], height = 40))
                        mainPanel.add(Box.createVerticalStrut(15))
                    }
                }
            }

            // ======= PDF Button =======
            val downloadButton = JButton("Download as PDF").apply {
                alignmentX = Component.LEFT_ALIGNMENT
                background = Color(30, 30, 30)
                foreground = Color.WHITE
                font = AwtFont("Arial", AwtFont.BOLD, 14)
                addActionListener {
                    val fileChooser = JFileChooser().apply {
                        dialogTitle = "Save PDF"
                        selectedFile = File("Overall Review.pdf")
                        fileFilter = FileNameExtensionFilter("PDF Documents", "pdf")
                    }
                    val userSelection = fileChooser.showSaveDialog(null)
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        var fileToSave = fileChooser.selectedFile
                        if (!fileToSave.name.endsWith(".pdf")) {
                            fileToSave = File(fileToSave.absolutePath + ".pdf")
                        }
                        try {
                            generatePdf(jsonElement, fileToSave)
                            JOptionPane.showMessageDialog(null, "PDF saved to: ${fileToSave.absolutePath}")
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                            JOptionPane.showMessageDialog(null, "Failed to save PDF: ${ex.message}")
                        }
                    }
                }
            }
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(downloadButton)

            val outerScroll = JBScrollPane(mainPanel).apply {
                verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
                preferredSize = Dimension(1100, 800)
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

    private fun createSectionLabel(title: String): JPanel {
        val label = JLabel(title).apply {
            font = AwtFont("Arial", AwtFont.BOLD, 18)
            foreground = LIGHT_TEXT
            horizontalAlignment = SwingConstants.CENTER
        }

        return JPanel(FlowLayout(FlowLayout.CENTER)).apply {
            background = DARK_BG
            alignmentX = Component.LEFT_ALIGNMENT // prevent shifting other components
            add(label)
        }
    }

    private fun createCard(label: String, content: String, height: Int = 120): JPanel {
        val card = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createLineBorder(LIGHT_TEXT)
            background = DARK_PANEL
        }
        card.add(createLabeledTextArea(label, content, height))
        return card
    }

    private fun createLabeledTextArea(label: String?, content: String, height: Int = 120): JPanel {
        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            alignmentX = Component.LEFT_ALIGNMENT
            background = DARK_PANEL
        }

        if (!label.isNullOrBlank()) {
            val labelComponent = JLabel(label).apply {
                font = AwtFont("Arial", AwtFont.BOLD, 14)
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
            font = AwtFont("Arial", AwtFont.PLAIN, 13)
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
        return JPanel(BorderLayout()).apply {
            background = DARK_BG
            add(JLabel(message).apply { foreground = Color.RED }, BorderLayout.CENTER)
            preferredSize = Dimension(800, 200)
        }
    }

    private fun generatePdf(json: JsonObject, file: File) {
        val document = Document(PageSize.A4.rotate())
        PdfWriter.getInstance(document, file.outputStream())
        document.open()

        // Convert HEX color #E9E5E5 to BaseColor
        val headingFont = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD)
        val titleFont = Font(Font.FontFamily.HELVETICA, 17f, Font.BOLD)
        val headerBgColor = BaseColor(0xE9, 0xE5, 0xE5)
        val cellFont = Font(Font.FontFamily.HELVETICA, 12f, Font.NORMAL)

        fun addMainTitle(title: String) {
            val titlePara = Paragraph(title, titleFont).apply {
                alignment = Element.ALIGN_CENTER
                spacingAfter = 12f
            }
            document.add(titlePara)
        }

        fun addSectionTitle(title: String) {
            val titlePara = Paragraph(title, headingFont).apply {
                alignment = Element.ALIGN_LEFT
                spacingAfter = 12f
            }
            document.add(titlePara)
        }

        fun addSummaryTable(quality: String, severity: String, remarks: String) {
            val table = PdfPTable(floatArrayOf(1f, 1f, 2f))
            table.widthPercentage = 100f
            table.spacingAfter = 12f

            val headers = listOf("Quality", "Severity", "Remarks")
            headers.forEach {
                val cell = PdfPCell(Phrase(it, headingFont))
                cell.backgroundColor = headerBgColor
                cell.setPadding(6f)
                table.addCell(cell)
            }

            listOf(quality, severity, remarks).forEach {
                val cell = PdfPCell(Phrase(it, cellFont))
                cell.setPadding(6f)
                table.addCell(cell)
            }

            document.add(table)
        }

        fun addIssuesTable(category: String, issues: JsonArray) {
            addSectionTitle(category)
            val table = PdfPTable(if (category == "Cloud Violations") 6 else 4)
            table.widthPercentage = 100f
            table.spacingAfter = 15f

            val headers = if (category == "Cloud Violations") {
                listOf("Identification", "Explanation", "Fix", "Violation Type", "Policy Reference", "Severity")
            } else {
                listOf("Identification", "Explanation", "Fix", "Severity")
            }
            
            headers.forEach {
                val cell = PdfPCell(Phrase(it, headingFont))
                cell.backgroundColor = headerBgColor
                cell.setPadding(6f)
                table.addCell(cell)
            }

            for (item in issues) {
                val obj = item.jsonObject
                val row = if (category == "Cloud Violations") {
                    listOf(
                        obj["identification"]?.jsonPrimitive?.content ?: "N/A",
                        obj["explanation"]?.jsonPrimitive?.content ?: "N/A",
                        obj["fix"]?.jsonPrimitive?.content ?: "N/A",
                        obj["violationType"]?.jsonPrimitive?.content ?: "N/A",
                        obj["policyReference"]?.jsonPrimitive?.content ?: "N/A",
                        obj["severity"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } else {
                    listOf(
                        obj["identification"]?.jsonPrimitive?.content ?: "N/A",
                        obj["explanation"]?.jsonPrimitive?.content ?: "N/A",
                        obj["fix"]?.jsonPrimitive?.content ?: "N/A",
                        obj["severity"]?.jsonPrimitive?.content ?: "N/A"
                    )
                }
                row.forEach {
                    val cell = PdfPCell(Phrase(it, cellFont))
                    cell.setPadding(6f)
                    table.addCell(cell)
                }
            }

            document.add(table)
        }

        addMainTitle("Overall Review")

        // ===== Summary Section =====
        addSectionTitle("Summary:")
        val quality = json["quality"]?.jsonPrimitive?.content ?: "N/A"
        val severity = json["overallSeverity"]?.jsonPrimitive?.content ?: "N/A"
        val remarks = json["remarks"]?.jsonPrimitive?.content ?: "N/A"
        addSummaryTable(quality, severity, remarks)

        // ===== Issues Section =====
        addSectionTitle("Issues:")
        val issues = json["issues"]?.jsonObject
        val categoryOrder = listOf("Quality", "Performance", "Security", "Organization Standards", "CK Metrics", "Syntax", "Cloud Violations")
        categoryOrder.forEach { category ->
            val issueList = issues?.get(category)?.jsonArray
            if (issueList != null && issueList.isNotEmpty()) {
                addIssuesTable(category, issueList)
            }
        }

        document.close()
    }

    // Colors
    private val DARK_BG = Color(60, 63, 65)
    private val DARK_PANEL = Color(75, 78, 80)
    private val DARK_TEXTAREA = Color(50, 53, 55)
    private val LIGHT_TEXT = Color(187, 187, 187)
}
