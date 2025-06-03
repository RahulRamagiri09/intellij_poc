package com.example.sidebarplugin.GitKB.GitKBResponse

import com.intellij.ui.components.JBScrollPane
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.PageSize
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import kotlinx.serialization.json.*
import java.awt.*
import java.io.File
import java.io.FileOutputStream
import javax.swing.*
import com.itextpdf.text.Font

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

            // ======= Download PDF Button =======
            val buttonPanel = JPanel().apply {
                layout = FlowLayout(FlowLayout.CENTER)
                background = JsonExplain.DARK_BG
            }

            val downloadButton = JButton("Download as PDF").apply {
                font = java.awt.Font("Arial", java.awt.Font.BOLD, 14)
                background = Color(100, 150, 200)
                foreground = Color.WHITE
                addActionListener {
                    saveAsPdf(response)
                }
            }

            buttonPanel.add(downloadButton)
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(buttonPanel)

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

    private fun saveAsPdf(jsonText: String) {
        val fileChooser = JFileChooser().apply {
            dialogTitle = "Save Assistant PDF"
            selectedFile = File("explain_gitKB_report.pdf")
        }

        val userSelection = fileChooser.showSaveDialog(null)
        if (userSelection != JFileChooser.APPROVE_OPTION) return

        val filePath = if (fileChooser.selectedFile.path.endsWith(".pdf")) {
            fileChooser.selectedFile.path
        } else {
            "${fileChooser.selectedFile.path}.pdf"
        }

        val document = Document(PageSize.A4.rotate()) // Landscape
        try {
            PdfWriter.getInstance(document, FileOutputStream(filePath))
            document.open()

            val headingFont = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD)
            val labelFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
            val normalFont = Font(Font.FontFamily.HELVETICA, 11f)
            val grayBg = BaseColor(233, 229, 229)

            document.add(Paragraph("Explain from Git KB", headingFont))
            document.add(Chunk.NEWLINE)

            val jsonElement = Json.parseToJsonElement(jsonText).jsonObject

            // ======= Summary Table (Quality + Remarks) =======
            document.add(Paragraph("Summary", headingFont))
            document.add(Paragraph(" "))

            val quality = extractArray(jsonElement["quality"]).joinToString(", ")
            val remarks = jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"

            val summaryTable = PdfPTable(2).apply {
                widthPercentage = 100f
                addHeaderCell("Quality", labelFont, grayBg)
                addHeaderCell("Remarks", labelFont, grayBg)
                addContentCell(quality, normalFont)
                addContentCell(remarks, normalFont)
                spacingAfter = 20f
            }

            document.add(summaryTable)

            // ======= Explanation Section =======
            document.add(Paragraph("Explanation", headingFont))
            document.add(Chunk.NEWLINE)

            val explanationArray = jsonElement["explanation"]?.jsonArray ?: JsonArray(emptyList())

            val expTable = PdfPTable(2).apply {
                widthPercentage = 100f
                headerRows = 1  // Repeat header on page breaks
                spacingBefore = 5f
                spacingAfter = 10f
                addHeaderCell("Overview", labelFont, grayBg)
                addHeaderCell("Detailed Explanation", labelFont, grayBg)
            }

            explanationArray.forEach { item ->
                val obj = item.jsonObject
                val overview = obj["overview"]?.jsonPrimitive?.content ?: "N/A"
                val detailed = obj["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
                expTable.addContentCell(overview, normalFont)
                expTable.addContentCell(detailed, normalFont)
            }

            // Add table after heading — it will split properly across pages
            document.add(expTable)

            document.close()
            JOptionPane.showMessageDialog(null, "PDF saved successfully at:\n$filePath", "Success", JOptionPane.INFORMATION_MESSAGE)
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, "Error saving PDF: ${e.message}", "Error", JOptionPane.ERROR_MESSAGE)
        }
    }

    private fun PdfPTable.addHeaderCell(text: String, font: Font, bgColor: BaseColor) {
        val cell = PdfPCell(Phrase(text, font)).apply {
            backgroundColor = bgColor
            paddingTop = 6f
            paddingBottom = 6f
            paddingLeft = 6f
            paddingRight = 6f
            horizontalAlignment = Element.ALIGN_CENTER
        }
        addCell(cell)
    }


    private fun PdfPTable.addContentCell(text: String, font: Font) {
        val cell = PdfPCell(Phrase(text, font)).apply {
            paddingTop = 6f
            paddingBottom = 6f
            paddingLeft = 6f
            paddingRight = 6f
            horizontalAlignment = Element.ALIGN_LEFT
        }
        addCell(cell)
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
            font = Font("Arial",  java.awt.Font.PLAIN,13)
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

