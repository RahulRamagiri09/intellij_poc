package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import kotlinx.serialization.json.*
import java.awt.*
import java.io.FileOutputStream
import javax.swing.*
import com.lowagie.text.Document
import com.lowagie.text.Paragraph
import com.lowagie.text.pdf.PdfWriter
import com.lowagie.text.FontFactory
import java.awt.Font as AwtFont
import com.lowagie.text.pdf.PdfPTable
import com.lowagie.text.pdf.PdfPCell
import com.lowagie.text.Phrase
import com.lowagie.text.PageSize
import com.itextpdf.text.Element

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
            val details = jsonElement["details"]?.jsonPrimitive?.content ?: "N/A"
            mainPanel.add(createSectionLabel("Details"))
            mainPanel.add(createCard("Description", details))
            mainPanel.add(Box.createVerticalStrut(20))

            // ======= Unit Test Cases Section =======
            mainPanel.add(createSectionLabel("Unit Test Cases"))
            val unitTestData = jsonElement["unitTests"]?.jsonArray ?: JsonArray(emptyList())
            val testCases = mutableListOf<UnitTestData>()
            var testIndex = 1

            for (test in unitTestData) {
                val testObject = test.jsonObject
                val testCase = testObject["testCase"]?.jsonPrimitive?.content ?: "N/A"
                val explanation = testObject["explanation"]?.jsonPrimitive?.content ?: "N/A"
                val importance = testObject["importance"]?.jsonPrimitive?.intOrNull?.toString() ?: "0"
                val severity = testObject["severity"]?.jsonPrimitive?.content ?: "N/A"

                testCases.add(UnitTestData(testCase, explanation, importance, severity))

                val card = JPanel().apply {
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    border = BorderFactory.createLineBorder(LIGHT_TEXT)
                    background = DARK_PANEL
                }

                card.add(createLabeledTextArea("$testIndex. Test Case", testCase, height = 150))
                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Explanation", explanation))
                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Importance", importance, height = 40))
                card.add(Box.createVerticalStrut(10))
                card.add(createLabeledTextArea("Severity", severity, height = 40))
                mainPanel.add(card)
                mainPanel.add(Box.createVerticalStrut(15))
                testIndex++
            }

            // ======= PDF Button =======
            val pdfButton = JButton("Download PDF").apply {
                background = Color(100, 149, 237)
                foreground = Color.WHITE
                font = AwtFont("Arial", AwtFont.BOLD, 14)
                alignmentX = Component.LEFT_ALIGNMENT
                addActionListener {
                    createPdf(details, testCases)
                    JOptionPane.showMessageDialog(null, "PDF saved as unit_test_report.pdf")
                }
            }
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(pdfButton)

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

        private fun createPdf(details: String, testCases: List<UnitTestData>) {
            val fileChooser = JFileChooser().apply {
                dialogTitle = "Save Unit Test PDF"
                selectedFile = java.io.File("unit_test_report.pdf")
            }

            val userSelection = fileChooser.showSaveDialog(null)
            if (userSelection != JFileChooser.APPROVE_OPTION) return

            val selectedFile = fileChooser.selectedFile
            val filePath = if (selectedFile.path.endsWith(".pdf")) {
                selectedFile.path
            } else {
                "${selectedFile.path}.pdf"
            }

            try {
                val document = Document(PageSize.A4.rotate()) // Landscape layout
                PdfWriter.getInstance(document, FileOutputStream(filePath))
                document.open()

                val headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18f)
                val labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12f)
                val contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12f)

                val headerBgColor = Color(233, 229, 229)

                // ====== Details Section ======
                document.add(Paragraph("Details", headingFont))
                document.add(Paragraph(" "))

                val detailsTable = PdfPTable(2)
                detailsTable.widthPercentage = 100f
                detailsTable.setWidths(floatArrayOf(1f, 5f))

                val detailsHeaderCell = PdfPCell(Phrase("Description", labelFont)).apply {
                    backgroundColor = headerBgColor
                    setPadding(6f)
                }
                detailsTable.addCell(detailsHeaderCell)

                detailsTable.addCell(PdfPCell(Phrase(details, contentFont)).apply {
                    setPadding(6f)
                })

                document.add(detailsTable)
                document.add(Paragraph("\n\n"))

                // ====== Unit Test Cases Table ======
                document.add(Paragraph("Unit Test Cases", headingFont))
                document.add(Paragraph(" "))

                val unitTestTable = PdfPTable(5)
                unitTestTable.widthPercentage = 100f
                unitTestTable.setWidths(floatArrayOf(1f, 4f, 4f, 2f, 2f))

                val headers = listOf("S.No", "Test Case", "Explanation", "Importance", "Severity")
                for (header in headers) {
                    val headerCell = PdfPCell(Phrase(header, labelFont)).apply {
                        backgroundColor = headerBgColor
                        horizontalAlignment = Element.ALIGN_CENTER
                        setPadding(6f)
                    }
                    unitTestTable.addCell(headerCell)
                }

                for ((i, test) in testCases.withIndex()) {
                    unitTestTable.addCell(PdfPCell(Phrase("${i + 1}", contentFont)).apply { setPadding(5f) })
                    unitTestTable.addCell(PdfPCell(Phrase(test.testCase, contentFont)).apply { setPadding(5f) })
                    unitTestTable.addCell(PdfPCell(Phrase(test.explanation, contentFont)).apply { setPadding(5f) })
                    unitTestTable.addCell(PdfPCell(Phrase(test.importance, contentFont)).apply { setPadding(5f) })
                    unitTestTable.addCell(PdfPCell(Phrase(test.severity, contentFont)).apply { setPadding(5f) })
                }

                document.add(unitTestTable)
                document.close()

                JOptionPane.showMessageDialog(null, "PDF successfully saved to:\n$filePath")
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(null, "Failed to save PDF: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
            }
        }

        private fun createSectionLabel(title: String): JLabel {
            return JLabel(title).apply {
                font = AwtFont("Arial", AwtFont.BOLD, 18)
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
            }

            if (!label.isNullOrBlank()) {
                val labelComponent = JLabel(label).apply {
                    font = AwtFont("Arial", AwtFont.BOLD, 14)
                    foreground = LIGHT_TEXT
                    alignmentX = Component.LEFT_ALIGNMENT
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
            }

            panel.add(scrollPane)
            return panel
        }

    private fun errorPanel(message: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.background = DARK_BG
        val label = JLabel(message).apply { foreground = Color.RED }
        panel.add(label, BorderLayout.CENTER)
        panel.preferredSize = Dimension(800, 200)
        return panel
    }

    // Colors
    private val DARK_BG = Color(60, 63, 65)
    private val DARK_PANEL = Color(75, 78, 80)
    private val DARK_TEXTAREA = Color(50, 53, 55)
    private val LIGHT_TEXT = Color(187, 187, 187)

    data class UnitTestData(
        val testCase: String,
        val explanation: String,
        val importance: String,
        val severity: String
    )
}
