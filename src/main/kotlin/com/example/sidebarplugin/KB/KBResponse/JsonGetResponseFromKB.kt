//package com.example.sidebarplugin.KB.KBResponse
//import kotlinx.serialization.json.*
//
//object JsonGetResponseFromKB {
//    fun extractGetResponseFromKB(response: String): String {
//        println("Raw API Response (Get Code): $response") // Debugging log
//
//        if (!response.trim().startsWith("{")) { // Check if response is valid JSON
//            return "Error: Response is not valid JSON: $response"
//        }
//
//        return try {
//            val jsonElement = Json.parseToJsonElement(response).jsonObject
//            jsonElement["response"]?.jsonPrimitive?.content ?: "No Get Code added."
//        } catch (e: Exception) {
//            "Invalid JSON response: ${e.message} (Response: $response)"
//        }
//    }
//}
//

package com.example.sidebarplugin.KB.KBResponse

import java.awt.*
import java.io.FileOutputStream
import java.nio.file.Paths
import javax.swing.*
import kotlinx.serialization.json.*
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter

object JsonGetResponseFromKB {
    fun extractGetResponseFromKB(response: String): JPanel {
        println("Raw API Response (Get Code): $response")

        val extractedText = try {
            if (!response.trim().startsWith("{")) {
                "Error: Response is not valid JSON: $response"
            } else {
                val jsonElement = Json.parseToJsonElement(response).jsonObject
                jsonElement["response"]?.jsonPrimitive?.content ?: "No Get Code added."
            }
        } catch (e: Exception) {
            "Invalid JSON response: ${e.message} (Response: $response)"
        }

        return buildResponsePanel(extractedText)
    }

    private fun buildResponsePanel(responseText: String): JPanel {
        val panel = JPanel(BorderLayout())
        panel.preferredSize = Dimension(600, 400)

        val textArea = JTextArea(responseText)
        textArea.lineWrap = true
        textArea.wrapStyleWord = true
        textArea.isEditable = false

        val scrollPane = JScrollPane(textArea)
        scrollPane.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED

//        val downloadButton = JButton("Download PDF")
//        downloadButton.addActionListener {
//            val fileChooser = JFileChooser()
//            fileChooser.dialogTitle = "Choose location to save PDF"
//            fileChooser.selectedFile = java.io.File("kb_response.pdf") // Default file name
//
//            val userSelection = fileChooser.showSaveDialog(panel)
//            if (userSelection == JFileChooser.APPROVE_OPTION) {
//                val fileToSave = fileChooser.selectedFile
//                try {
//                    val document = com.itextpdf.text.Document()
//                    com.itextpdf.text.pdf.PdfWriter.getInstance(document, FileOutputStream(fileToSave))
//                    document.open()
//                    document.add(com.itextpdf.text.Paragraph(responseText))
//                    document.close()
//
//                    JOptionPane.showMessageDialog(null, "PDF saved to: ${fileToSave.absolutePath}")
//                } catch (e: Exception) {
//                    JOptionPane.showMessageDialog(null, "Error saving PDF: ${e.message}")
//                }
//            }
//        }
        val downloadButton = JButton("Download PDF").apply {
            background = Color(100, 100, 255)
            foreground = Color.WHITE
            isFocusPainted = false
            border = BorderFactory.createEmptyBorder()
            font = Font("Arial", Font.BOLD, 14)

            addActionListener {
                val fileChooser = JFileChooser()
                fileChooser.dialogTitle = "Choose location to save PDF"
                fileChooser.selectedFile = java.io.File("kb_response.pdf")

                val userSelection = fileChooser.showSaveDialog(panel)
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    val fileToSave = fileChooser.selectedFile
                    try {
                        val document = com.itextpdf.text.Document()
                        com.itextpdf.text.pdf.PdfWriter.getInstance(document, FileOutputStream(fileToSave))
                        document.open()
                        document.add(com.itextpdf.text.Paragraph(responseText))
                        document.close()

                        JOptionPane.showMessageDialog(null, "PDF saved to: ${fileToSave.absolutePath}")
                    } catch (e: Exception) {
                        JOptionPane.showMessageDialog(null, "Error saving PDF: ${e.message}")
                    }
                }
            }
        }

        val buttonPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        buttonPanel.add(downloadButton)

        panel.add(scrollPane, BorderLayout.CENTER)
        panel.add(buttonPanel, BorderLayout.SOUTH)

        return panel
    }
}
