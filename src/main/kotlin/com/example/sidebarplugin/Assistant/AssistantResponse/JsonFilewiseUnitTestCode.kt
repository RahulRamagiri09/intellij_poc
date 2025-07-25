package com.example.sidebarplugin.Assistant.AssistantResponse

import kotlinx.serialization.json.*
import java.awt.*
import javax.swing.*
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import com.intellij.ui.components.JBScrollPane
import javax.swing.filechooser.FileNameExtensionFilter
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import java.io.File
import java.awt.Font as AwtFont

object JsonFilewiseUnitTestCode {

    fun extractJobID(response: String): String {
        println("Raw JobID response: $response")
        return try {
            val jsonElement = Json.parseToJsonElement(response.trim()).jsonObject
            val jobId = jsonElement["JobID"]?.jsonPrimitive?.content
            println("Parsed JobID: $jobId")
            jobId ?: "JobID not found"
        } catch (e: Exception) {
            println("Failed to parse JSON, fallback to raw string. Error: ${e.message}")
            response.trim()
        }
    }

    private fun httpPostJson(urlStr: String, jsonBody: String, authToken: String): String {
        println("Hitting POST URL: $urlStr with body: $jsonBody")
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connection.connectTimeout = 5000
        connection.readTimeout = 5000
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "Bearer $authToken")

        OutputStreamWriter(connection.outputStream).use { writer ->
            writer.write(jsonBody)
            writer.flush()
        }

        val response = connection.inputStream.bufferedReader().use { it.readText() }
        println("Response from $urlStr:\n$response")
        return response
    }

    fun pollForResults(
        statusUrl: String,
        jobId: String,
        authToken: String,
        maxRetries: Int = 20,
        delayMillis: Long = 10_000,
        isCancelled: () -> Boolean = { false }
    ): String {
        println("Starting pollForResults() with JobID: $jobId")

        repeat(maxRetries) { attempt ->
            println("Polling attempt ${attempt + 1}/$maxRetries...")

            if (isCancelled()) {
                println("Polling cancelled by user.")
                return "Cancelled"
            }

            try {
                val jsonBody = JsonObject(mapOf("JobID" to JsonPrimitive(jobId))).toString()
                val response = httpPostJson(statusUrl, jsonBody, authToken)

                val json = Json.parseToJsonElement(response).jsonObject
                val status = json["status"]?.jsonPrimitive?.content ?: "UNKNOWN"


                when {
                    status.equals("COMPLETED", ignoreCase = true) -> {
                        val results = json["results"]?.toString() ?: "No results found."
                        println("Job completed. Returning results.")
                        return results
                    }

                    status.equals("FAILED", ignoreCase = true) -> {
                        println("Job failed. Returning error response.")
                        return response // Return full JSON so renderResultsPanel() can handle it
                    }
                }

            } catch (e: Exception) {
                println("Polling error: ${e.message}")
            }

            Thread.sleep(delayMillis)
        }

        println("Timeout reached. Job did not complete.")
        return "Timeout: Job did not complete in time."
    }
fun renderResultsPanel(resultsJson: String, language: String = ""): JPanel {
    val mainPanel = JPanel().apply {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        background = DARK_BG
    }

    try {
        val root = Json.parseToJsonElement(resultsJson).jsonObject
        val status = root["status"]?.jsonPrimitive?.content?.uppercase() ?: "UNKNOWN"

        if (status == "FAILED") {
            val statusDisplay = root["Status_display"]?.jsonPrimitive?.content ?: "Job failed with no additional info."
            mainPanel.add(createSectionLabel(" Job Failed"))
            mainPanel.add(createCard("Failure Details", statusDisplay.trim(), height = 300))
            return wrapWithScroll(mainPanel)
        }

        val testcases = root["testcases"]?.jsonArray ?: return errorPanel("No test cases found.")
        val checkboxTestcases = mutableListOf<Pair<JCheckBox, JsonObject>>()

        mainPanel.add(createSectionLabel("Filewise Unit Test Cases:"))

        // ✅ Add Select All checkbox
        val selectAllCheckbox = JCheckBox("Select All").apply {
            isSelected = false
            foreground = LIGHT_TEXT
            background = DARK_BG
        }
        val selectAllPanel = JPanel().apply {
            layout = FlowLayout(FlowLayout.LEFT)
            background = DARK_BG
            alignmentX = Component.LEFT_ALIGNMENT
            add(selectAllCheckbox)
        }
        mainPanel.add(selectAllPanel)

        testcases.forEachIndexed { index, test ->
            val testObj = test.jsonObject
            val description = testObj["description"]?.jsonPrimitive?.content ?: "N/A"
            val testcase = testObj["testcase"]?.jsonPrimitive?.content ?: "N/A"
            val data = testObj["data"]?.jsonArray?.joinToString("\n") { it.toString() } ?: "N/A"

            val checkbox = JCheckBox("Include this test case").apply {
                isSelected = false
                foreground = LIGHT_TEXT
                background = DARK_BG
            }
            checkboxTestcases.add(Pair(checkbox, testObj))

            val casePanel = JPanel().apply {
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                background = DARK_BG
                alignmentX = Component.LEFT_ALIGNMENT
                border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
            }

            casePanel.add(checkbox)
            casePanel.add(createCard("Description", description, height = 60))
            casePanel.add(createCard("Data", data, height = 80))
            casePanel.add(createCard("Test Case", testcase, height = 150))
            casePanel.add(Box.createVerticalStrut(20))

            mainPanel.add(casePanel)
        }

        // ✅ Select All checkbox behavior
        selectAllCheckbox.addActionListener {
            val select = selectAllCheckbox.isSelected
            checkboxTestcases.forEach { it.first.isSelected = select }
        }

        val buttonPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            background = DARK_BG
            alignmentX = Component.LEFT_ALIGNMENT
        }

        val downloadButton = JButton("Download PDF").apply {
            background = Color(100, 100, 255)
            foreground = Color.WHITE
            font = AwtFont("Arial", AwtFont.BOLD, 14)
            addActionListener {
                val selected = checkboxTestcases.filter { it.first.isSelected }.map { it.second }
                if (selected.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select at least one test case.")
                    return@addActionListener
                }
                generatePdf(selected)
            }
        }
        buttonPanel.add(downloadButton)
        buttonPanel.add(Box.createHorizontalStrut(10))

        val codeExtension = when (language.lowercase()) {
            "python" -> "py"
            "java" -> "java"
            else -> null
        }

        if (codeExtension != null) {
            val downloadCodeButton = JButton("Download .$codeExtension").apply {
                background = Color(100, 100, 255)
                foreground = Color.WHITE
                font = AwtFont("Arial", AwtFont.BOLD, 14)

                addActionListener {
                    val selected = checkboxTestcases.filter { it.first.isSelected }.map { it.second }
                    if (selected.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please select at least one test case.")
                        return@addActionListener
                    }

                    val fileChooser = JFileChooser().apply {
                        selectedFile = File("Filewise Unit Test Code.$codeExtension")
                    }

                    if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return@addActionListener

                    try {
                        val file = fileChooser.selectedFile
                        val contentBuilder = StringBuilder()

                        val imports = root["imports"]?.jsonArray
                        imports?.forEach { imp -> contentBuilder.appendLine(imp.jsonPrimitive.content) }
                        if (!imports.isNullOrEmpty()) contentBuilder.appendLine("\n")

                        selected.forEach { testObj ->
                            val description = testObj["description"]?.jsonPrimitive?.content ?: "N/A"
                            val data = testObj["data"]?.jsonArray?.joinToString(", ") { it.toString() } ?: "N/A"
                            val testcase = testObj["testcase"]?.jsonPrimitive?.content ?: "N/A"

                            val commentPrefix = when (language.lowercase()) {
                                "python" -> "#"
                                "java" -> "//"
                                else -> "//"
                            }

                            contentBuilder.appendLine("$commentPrefix Description: $description")
                            contentBuilder.appendLine("$commentPrefix Data: $data")
                            contentBuilder.appendLine()
                            contentBuilder.appendLine(testcase)
                            contentBuilder.appendLine()
                            contentBuilder.appendLine()
                        }

                        file.writeText(contentBuilder.toString())
                        JOptionPane.showMessageDialog(null, "Code saved successfully to: ${file.absolutePath}")
                    } catch (e: Exception) {
                        JOptionPane.showMessageDialog(null, "Failed to save code: ${e.message}")
                    }
                }
            }
            buttonPanel.add(downloadCodeButton)
        }

        mainPanel.add(Box.createVerticalStrut(10))
        mainPanel.add(buttonPanel)
        mainPanel.revalidate()
        mainPanel.repaint()

    } catch (e: Exception) {
        return errorPanel("Failed to parse test results: ${e.message}")
    }

    return wrapWithScroll(mainPanel)
}


    private fun generatePdf(testcases: List<JsonObject>) {
        try {
            val fileChooser = JFileChooser().apply {
                fileFilter = FileNameExtensionFilter("PDF files", "pdf")
                selectedFile = File("Filewise Unit Test Code.pdf")
            }

            if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) return

            val file = fileChooser.selectedFile
            val document = Document(PageSize.A4.rotate())  // Landscape orientation
            PdfWriter.getInstance(document, file.outputStream())
            document.open()

            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD)
            val headerFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
            val cellFont = Font(Font.FontFamily.HELVETICA, 11f)

            document.add(Paragraph("Filewise Unit Test Cases", titleFont).apply {
                spacingAfter = 20f
            })

            val table = PdfPTable(5)
            table.widthPercentage = 100f
            table.setWidths(floatArrayOf(2f, 2f, 3f, 1f, 1.5f)) // Custom column widths

            val headers = listOf("Description", "Data", "Test Case", "Confidence Score", "Intervention Needed")
            headers.forEach {
                val cell = PdfPCell(Phrase(it, headerFont)).apply {
                    backgroundColor = BaseColor.LIGHT_GRAY
                    horizontalAlignment = Element.ALIGN_CENTER

                }
                table.addCell(cell)
            }

            testcases.forEach { test ->
                val description = test["description"]?.jsonPrimitive?.content ?: "N/A"
                val data = test["data"]?.jsonArray?.joinToString("\n") { it.toString() } ?: "N/A"
                val testcase = test["testcase"]?.jsonPrimitive?.content ?: "N/A"
                val score = test["confidence_score"]?.jsonPrimitive?.content ?: "N/A"
                val intervention = test["intervention_needed"]?.jsonPrimitive?.content ?: "None"

                val cells = listOf(description, data, testcase, score, intervention)
                cells.forEach { value ->
                    val cell = PdfPCell(Phrase(value, cellFont)).apply {

                        horizontalAlignment = Element.ALIGN_LEFT
                    }
                    table.addCell(cell)
                }
            }

            document.add(table)
            document.close()
            JOptionPane.showMessageDialog(null, "PDF saved successfully to: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "Failed to generate PDF: ${e.message}")
        }
    }



    private fun wrapWithScroll(contentPanel: JPanel): JPanel {
        val outerScroll = JBScrollPane(contentPanel).apply {
            verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
            horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            preferredSize = Dimension(1200, 800)
            background = DARK_BG
        }

        return JPanel(BorderLayout()).apply {
            background = DARK_BG
            add(outerScroll, BorderLayout.CENTER)
        }
    }


    // Helper methods for UI layout and theme
    private fun createSectionLabel(title: String): JLabel {
        return JLabel(title).apply {
            font = AwtFont("Arial", AwtFont.BOLD, 18)
            foreground = LIGHT_TEXT
            alignmentX = Component.LEFT_ALIGNMENT
            border = BorderFactory.createEmptyBorder(10, 10, 5, 10)
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
            lineWrap = false
            wrapStyleWord = false
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

    // Theme colors
    private val DARK_BG = Color(60, 63, 65)
    private val DARK_PANEL = Color(75, 78, 80)
    private val DARK_TEXTAREA = Color(50, 53, 55)
    private val LIGHT_TEXT = Color(187, 187, 187)
}


