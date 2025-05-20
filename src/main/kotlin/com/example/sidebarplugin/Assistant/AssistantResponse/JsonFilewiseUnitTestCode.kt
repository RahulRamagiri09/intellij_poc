package com.example.sidebarplugin.Assistant.AssistantResponse

import kotlinx.serialization.json.*
import java.awt.*
import javax.swing.*
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

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
        delayMillis: Long = 10_000
    ): String {
        println("Starting pollForResults() with JobID: $jobId")

        repeat(maxRetries) { attempt ->
            println("Polling attempt ${attempt + 1}/$maxRetries...")
            try {
                val jsonBody = JsonObject(mapOf("JobID" to JsonPrimitive(jobId))).toString()
                val response = httpPostJson(statusUrl, jsonBody, authToken)

                val json = Json.parseToJsonElement(response).jsonObject
                val status = json["status"]?.jsonPrimitive?.content ?: "UNKNOWN"
                println("Status: $status")

                if (status.equals("COMPLETED", ignoreCase = true)) {
                    val results = json["results"]?.toString() ?: "No results found."
                    println("Job completed. Returning results.")
                    return results
                }
            } catch (e: Exception) {
                println("Polling error: ${e.message}")
            }

            Thread.sleep(delayMillis)
        }

        println("Timeout reached. Job did not complete.")
        return "Timeout: Job did not complete in time."
    }

    fun renderResultsPanel(resultsJson: String): JPanel {
        val mainPanel = JPanel()
        mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
        mainPanel.background = Color(0x1E1E1E)

        try {
            val root = Json.parseToJsonElement(resultsJson).jsonObject
            val testcases = root["testcases"]?.jsonArray ?: return mainPanel

            for (test in testcases) {
                val testObj = test.jsonObject
                val description = testObj["description"]?.jsonPrimitive?.content ?: "No Description"
                val dataArray = testObj["data"]?.jsonArray
                val data = dataArray?.joinToString("\n") { inner ->
                    if (inner is JsonArray) {
                        inner.joinToString(", ") { item ->
                            if (item is JsonPrimitive) item.content
                            else item.toString()
                        }
                    } else {
                        if (inner is JsonPrimitive) inner.content else inner.toString()
                    }
                } ?: "No Data"



                val testcase = testObj["testcase"]?.jsonPrimitive?.content ?: "No Test Case"

                val card = JPanel()
                card.layout = BoxLayout(card, BoxLayout.Y_AXIS)
                card.border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
                card.background = Color(0x2D2D30)

                val descLabel = JLabel("<html><b style='color:#ffffff'>Description:</b> <span style='color:#cccccc'>$description</span></html>")
                descLabel.alignmentX = Component.LEFT_ALIGNMENT
                descLabel.horizontalAlignment = SwingConstants.LEFT
                descLabel.border = BorderFactory.createEmptyBorder(0, 0, 0, 0) // Remove any extra space
                descLabel.setAlignmentX(Component.LEFT_ALIGNMENT) // Explicitly align it left
                descLabel.setOpaque(true) // Ensure proper rendering
                descLabel.background = Color(0x2D2D30) // Match the panel background
                card.add(descLabel)


                val dataArea = JTextArea(data)
                dataArea.isEditable = false
                dataArea.background = Color(0x1E1E1E)
                dataArea.foreground = Color(0x9CDCFE)
                dataArea.font = Font("Consolas", Font.PLAIN, 13)
                dataArea.margin = Insets(5, 5, 5, 5)
                dataArea.lineWrap = true
                dataArea.wrapStyleWord = true

                val dataScroll = JScrollPane(dataArea)
                dataScroll.preferredSize = Dimension(600, 80)
                card.add(dataScroll)
                card.add(Box.createVerticalStrut(5))

                val codeArea = JTextArea(testcase)
                codeArea.isEditable = false
                codeArea.background = Color(0x1E1E1E)
                codeArea.foreground = Color(0xDCDCAA)
                codeArea.font = Font("Consolas", Font.PLAIN, 13)
                codeArea.margin = Insets(5, 5, 5, 5)
                codeArea.lineWrap = false
                codeArea.wrapStyleWord = false

                val codeScroll = JScrollPane(codeArea)
                codeScroll.preferredSize = Dimension(600, 250)
                card.add(codeScroll)
                card.add(Box.createVerticalStrut(15))

                mainPanel.add(card)
//                mainPanel.add(Box.createVerticalStrut(10))
                mainPanel.add(Box.createVerticalStrut(2))

            }
        } catch (e: Exception) {
            val errorLabel = JLabel("Error parsing test cases: ${e.message}")
            errorLabel.foreground = Color.RED
            mainPanel.add(errorLabel)
        }

        val scrollPane = JScrollPane(mainPanel)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        scrollPane.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED

        val wrapperPanel = JPanel(BorderLayout())
        wrapperPanel.add(scrollPane, BorderLayout.CENTER)

        return wrapperPanel
    }
}

