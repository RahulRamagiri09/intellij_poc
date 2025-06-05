package com.example.sidebarplugin.Assistant.AssistantResponse

import com.intellij.ui.components.JBScrollPane
import kotlinx.serialization.json.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfWriter
import java.awt.*
import java.io.FileOutputStream
import javax.swing.*
import com.itextpdf.text.Font
import java.io.File

import com.itextpdf.text.Document
import com.itextpdf.text.Chunk
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Phrase
import com.itextpdf.text.Element
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfPCell



object JsonExplainCode {
    fun extractExplainCode(response: String): JPanel {
        println("Raw API Response (JsonExplainCode): $response")

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
            val qualityArray = extractArray(jsonElement["quality"])
            qualityArray.forEach { mainPanel.add(createCard("Quality", it)) }

            val remarksArray = extractArray(jsonElement["remarks"])
            remarksArray.forEach { mainPanel.add(createCard("Remarks", it)) }

            // ======= Explanation Section =======
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(createSectionLabel("Explanation"))

            val explanationArray = jsonElement["explanation"]?.jsonArray ?: JsonArray(emptyList())
            explanationArray.forEach { item ->
                val obj = item.jsonObject
                mainPanel.add(createCard("Overview", obj["overview"]?.jsonPrimitive?.content ?: "N/A"))
                mainPanel.add(createCard("Detailed Explanation", obj["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"))
            }

            // ======= Key Components Section =======
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(createSectionLabel("Key Components"))

            val keyComponentsData = explanationArray.flatMap { explanation ->
                explanation.jsonObject["keyComponents"]?.jsonArray?.map { component ->
                    val compObj = component.jsonObject
                    Pair(
                        compObj["name"]?.jsonPrimitive?.content ?: "N/A",
                        compObj["description"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } ?: emptyList()
            }

            keyComponentsData.forEach { (name, description) ->
                mainPanel.add(createCard("Name", name))
                mainPanel.add(createCard("Description", description))
            }

            // ======= Logic Flow Section =======
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(createSectionLabel("Logic Flow"))

            val logicFlowData = explanationArray.flatMap { explanation ->
                explanation.jsonObject["logicFlow"]?.jsonArray?.map { step ->
                    val stepObj = step.jsonObject
                    Pair(
                        stepObj["step"]?.jsonPrimitive?.content ?: "N/A",
                        stepObj["purpose"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } ?: emptyList()
            }

            logicFlowData.forEach { (step, purpose) ->
                mainPanel.add(createCard("Step", step))
                mainPanel.add(createCard("Purpose", purpose))
            }

            // ======= Algorithms Section =======
            mainPanel.add(Box.createVerticalStrut(20))
            mainPanel.add(createSectionLabel("Algorithms"))

            val algorithmsData = explanationArray.flatMap { explanation ->
                explanation.jsonObject["algorithms"]?.jsonArray?.map { algo ->
                    val algoObj = algo.jsonObject
                    Pair(
                        algoObj["name"]?.jsonPrimitive?.content ?: "N/A",
                        algoObj["description"]?.jsonPrimitive?.content ?: "N/A"
                    )
                } ?: emptyList()
            }

            algorithmsData.forEach { (name, description) ->
                mainPanel.add(createCard("Algorithm", name))
                mainPanel.add(createCard("Description", description))
            }

            // ======= Download PDF Button =======
            val buttonPanel = JPanel().apply {
                layout = FlowLayout(FlowLayout.LEFT)
                background = DARK_BG
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
                preferredSize = Dimension(1100, 800)
                background = DARK_BG
            }

            JPanel(BorderLayout()).apply {
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
            selectedFile = File("Explain Code.pdf")
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
            val titleFont = Font(Font.FontFamily.HELVETICA, 17f, Font.BOLD)
            val labelFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
            val normalFont = Font(Font.FontFamily.HELVETICA, 11f)
            val grayBg = BaseColor(233, 229, 229)

            val title = Paragraph("Explain Code Assistant", titleFont).apply {
                alignment = Element.ALIGN_CENTER
                spacingAfter = 20f
            }
            document.add(title)

            val jsonElement = Json.parseToJsonElement(jsonText).jsonObject

            // Quality & Remarks
            document.add(Paragraph("Summary:", headingFont))
            document.add(Paragraph(" "))
            val quality = extractArray(jsonElement["quality"]).joinToString(", ")
            val remarks = jsonElement["remarks"]?.jsonPrimitive?.content ?: "N/A"
            val qrTable = PdfPTable(2).apply {
                widthPercentage = 100f
                addHeaderCell("Quality", labelFont, grayBg)
                addHeaderCell("Remarks", labelFont, grayBg)
                addContentCell(quality, normalFont)
                addContentCell(remarks, normalFont)
            }
            document.add(qrTable)
            document.add(Chunk.NEWLINE)

            val explanationArray = jsonElement["explanation"]?.jsonArray ?: JsonArray(emptyList())
            explanationArray.forEachIndexed { index, item ->
                val obj = item.jsonObject

                document.add(Paragraph("Explanation", headingFont))
//                document.add(Chunk.NEWLINE)
                document.add(Paragraph(" "))
                // Overview + Detailed Explanation
                val overview = obj["overview"]?.jsonPrimitive?.content ?: "N/A"
                val detailed = obj["detailedExplanation"]?.jsonPrimitive?.content ?: "N/A"
                val expTable = PdfPTable(2).apply {
                    widthPercentage = 100f
                    addHeaderCell("Overview", labelFont, grayBg)
                    addHeaderCell("Detailed Explanation", labelFont, grayBg)
                    addContentCell(overview, normalFont)
                    addContentCell(detailed, normalFont)
                }
                document.add(expTable)
                document.add(Chunk.NEWLINE)

                // Key Components
                document.add(Paragraph("Key Components", labelFont))
                document.add(Paragraph(" "))
                val kcTable = PdfPTable(2).apply {
                    widthPercentage = 100f
                    addHeaderCell("Name", labelFont, grayBg)
                    addHeaderCell("Description", labelFont, grayBg)
                    obj["keyComponents"]?.jsonArray?.forEach { comp ->
                        val c = comp.jsonObject
                        addContentCell(c["name"]?.jsonPrimitive?.content ?: "N/A", normalFont)
                        addContentCell(c["description"]?.jsonPrimitive?.content ?: "N/A", normalFont)
                    }
                }
                document.add(kcTable)
                document.add(Chunk.NEWLINE)

                // Logic Flow
                document.add(Paragraph("Logic Flow", labelFont))
                document.add(Paragraph(" "))
                val lfTable = PdfPTable(2).apply {
                    widthPercentage = 100f
                    addHeaderCell("Step", labelFont, grayBg)
                    addHeaderCell("Purpose", labelFont, grayBg)
                    obj["logicFlow"]?.jsonArray?.forEach { step ->
                        val s = step.jsonObject
                        addContentCell(s["step"]?.jsonPrimitive?.content ?: "N/A", normalFont)
                        addContentCell(s["purpose"]?.jsonPrimitive?.content ?: "N/A", normalFont)
                    }
                }
                document.add(lfTable)
                document.add(Chunk.NEWLINE)

                // Algorithms
                document.add(Paragraph("Algorithms", labelFont))
                document.add(Paragraph(" "))
                val algoTable = PdfPTable(2).apply {
                    widthPercentage = 100f
                    addHeaderCell("Name", labelFont, grayBg)
                    addHeaderCell("Description", labelFont, grayBg)
                    obj["algorithms"]?.jsonArray?.forEach { algo ->
                        val a = algo.jsonObject
                        addContentCell(a["name"]?.jsonPrimitive?.content ?: "N/A", normalFont)
                        addContentCell(a["description"]?.jsonPrimitive?.content ?: "N/A", normalFont)
                    }
                }
                document.add(algoTable)
                document.add(Chunk.NEWLINE)
            }

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
            font = java.awt.Font("Arial", java.awt.Font.BOLD, 18)
            foreground = LIGHT_TEXT
            border = BorderFactory.createEmptyBorder(10, 10, 5, 10)
        }
    }

    private fun createCard(label: String?, content: String): JPanel {
        val panel = JPanel(GridBagLayout())
        panel.background = DARK_PANEL
        panel.border = BorderFactory.createLineBorder(LIGHT_TEXT)

        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            anchor = GridBagConstraints.WEST
            insets = Insets(5, 10, 5, 10)
            fill = GridBagConstraints.HORIZONTAL
            weightx = 1.0
        }

        if (!label.isNullOrBlank()) {
            val labelComponent = JLabel(label).apply {
                font = java.awt.Font("Arial", Font.BOLD, 14)
                foreground = LIGHT_TEXT
            }
            panel.add(labelComponent, gbc)
            gbc.gridy++
        }

        val textArea = JTextArea(content).apply {
            lineWrap = true
            wrapStyleWord = true
            isEditable = false
            font = java.awt.Font("Arial", java.awt.Font.PLAIN, 13)
            background = DARK_TEXTAREA
            foreground = LIGHT_TEXT
            border = BorderFactory.createEmptyBorder(8, 8, 8, 8)
        }

        val scrollPane = JBScrollPane(textArea).apply {
            preferredSize = Dimension(850, 120)
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            background = DARK_PANEL
        }

        panel.add(scrollPane, gbc)
        return panel
    }


    private fun createLabeledTextArea(label: String?, content: String, height: Int = 120): JPanel {
        val panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = DARK_PANEL
        }

        label?.let {
            val labelComponent = JLabel(it).apply {
                font = java.awt.Font("Arial", java.awt.Font.BOLD, 14)
                foreground = LIGHT_TEXT
            }
            panel.add(labelComponent)
        }

        val textArea = JTextArea(content).apply {
            lineWrap = true
            wrapStyleWord = true
            isEditable = false
            font = java.awt.Font("Arial", java.awt.Font.PLAIN, 13)
            background = DARK_TEXTAREA
            foreground = LIGHT_TEXT
        }

        val scrollPane = JBScrollPane(textArea).apply {
            preferredSize = Dimension(850, height)
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        }

        panel.add(scrollPane)
        return panel
    }

    private fun errorPanel(message: String): JPanel {
        return JPanel(BorderLayout()).apply {
            background = DARK_BG
            add(JLabel(message).apply { foreground = Color.RED }, BorderLayout.CENTER)
        }
    }

    private val DARK_BG = Color(60, 63, 65)
    private val DARK_PANEL = Color(75, 78, 80)
    private val DARK_TEXTAREA = Color(50, 53, 55)
    private val LIGHT_TEXT = Color(187, 187, 187)
}

