package com.example.sidebarplugin.ui

import com.example.sidebarplugin.storage.PersistentState
import com.intellij.openapi.components.ServiceManager
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.awt.*
import java.io.IOException
import javax.swing.*
import javax.swing.border.EmptyBorder

fun createRepoDocumentationUI(onAccept: () -> Unit, onReject: () -> Unit): JPanel {
    val persistentState = ServiceManager.getService(PersistentState::class.java)
    val authToken = persistentState.getAuthToken() ?: ""
    val storedUrl = persistentState.getStoredUrl()?.trimEnd('/') ?: ""

    val mainPanel = JPanel()
    mainPanel.layout = BoxLayout(mainPanel, BoxLayout.Y_AXIS)
    mainPanel.border = EmptyBorder(20, 20, 20, 20)

    val titleLabel = JLabel("📘 Repository Documentation")
    titleLabel.font = titleLabel.font.deriveFont(20f)
    titleLabel.alignmentX = Component.CENTER_ALIGNMENT
    mainPanel.add(titleLabel)

    mainPanel.add(Box.createVerticalStrut(15))

    val subtitle = JLabel("Generate documentation by providing the following details:")
    subtitle.alignmentX = Component.CENTER_ALIGNMENT
    mainPanel.add(subtitle)
    mainPanel.add(Box.createVerticalStrut(30))

    val formPanel = JPanel()
    formPanel.layout = BoxLayout(formPanel, BoxLayout.Y_AXIS)
    formPanel.alignmentX = Component.CENTER_ALIGNMENT

    fun createLabeledField(labelText: String): JTextField {
        val label = JLabel(labelText)
        label.alignmentX = Component.CENTER_ALIGNMENT

        val textField = JTextField(30)
        textField.maximumSize = Dimension(300, 24)
        textField.alignmentX = Component.CENTER_ALIGNMENT

        val fieldPanel = JPanel()
        fieldPanel.layout = BoxLayout(fieldPanel, BoxLayout.Y_AXIS)
        fieldPanel.border = EmptyBorder(15, 0, 10, 0)
        fieldPanel.alignmentX = Component.CENTER_ALIGNMENT

        fieldPanel.add(label)
        fieldPanel.add(Box.createVerticalStrut(4))
        fieldPanel.add(textField)

        formPanel.add(fieldPanel)

        return textField
    }

    val repoField = createLabeledField("Repository URL *")
    val patField = createLabeledField("Personal Access Token (PAT)")
    val branchField = createLabeledField("Branch Name *")

    mainPanel.add(formPanel)

    val statusLabel = JLabel(" ")
    statusLabel.foreground = Color.RED
    statusLabel.alignmentX = Component.CENTER_ALIGNMENT
    mainPanel.add(Box.createVerticalStrut(10))
    mainPanel.add(statusLabel)
    mainPanel.add(Box.createVerticalStrut(10))

    val markdownArea = JTextArea(10, 80)
    markdownArea.isEditable = false
    markdownArea.lineWrap = true
    markdownArea.wrapStyleWord = true
    val scrollPane = JScrollPane(markdownArea)
    scrollPane.isVisible = false
    mainPanel.add(scrollPane)

    val buttonPanel = JPanel(FlowLayout(FlowLayout.CENTER))
    val submitButton = JButton("Submit")
    val cancelButton = JButton("Cancel")
    buttonPanel.add(submitButton)
    buttonPanel.add(cancelButton)
    mainPanel.add(buttonPanel)

    val client = OkHttpClient()
    val mediaType = "application/json; charset=utf-8".toMediaType()

    fun downloadMarkdown(jobID: String) {
        val request = Request.Builder()
            .url("$storedUrl/assistant/repo-documentation/download/$jobID")
            .get()
            .addHeader("Authorization", "Bearer $authToken")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to download markdown: ${e.message}")
                SwingUtilities.invokeLater {
                    statusLabel.text = "Failed to download markdown: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        println("Download markdown failed: HTTP ${response.code}")
                        SwingUtilities.invokeLater {
                            statusLabel.text = "Download failed: HTTP ${response.code}"
                        }
                        return
                    }

                    val markdownContent = it.body?.string() ?: ""
                    println("Downloaded Markdown content:\n$markdownContent")

                    SwingUtilities.invokeLater {
                        markdownArea.text = markdownContent
                        scrollPane.isVisible = true
                        statusLabel.text = "Documentation downloaded successfully."
                        mainPanel.revalidate()
                    }
                }
            }
        })
    }

    fun pollJobStatusUntilCompleted(jobID: String) {
        fun poll() {
            val json = JSONObject().apply { put("JobID", jobID) }
            val requestBody = json.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("$storedUrl/assistant/repo-documentation/status")
                .post(requestBody)
                .addHeader("Authorization", "Bearer $authToken")
                .addHeader("Content-Type", "application/json")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println("Status check failed: ${e.message}")
                    SwingUtilities.invokeLater {
                        statusLabel.text = "Status check failed: ${e.message}"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!it.isSuccessful) {
                            println("Failed to get job status: ${response.code}")
                            SwingUtilities.invokeLater {
                                statusLabel.text = "Failed to get job status: HTTP ${response.code}"
                            }
                            return
                        }

                        val responseBody = it.body?.string()
                        println("Job Status Response: $responseBody")

                        try {
                            val jsonResp = JSONObject(responseBody ?: "{}")
                            val status = jsonResp.optString("status")

                            if (status.equals("completed", ignoreCase = true)) {
                                println("Job completed!")
                                SwingUtilities.invokeLater {
                                    statusLabel.text = "Job completed. Downloading documentation..."
                                }
                                downloadMarkdown(jobID)
                            } else {
                                // Wait 3 seconds and poll again
                                Thread.sleep(3000)
                                poll()
                            }
                        } catch (e: Exception) {
                            println("Failed to parse status: ${e.message}")
                            SwingUtilities.invokeLater {
                                statusLabel.text = "Failed to parse status response."
                            }
                        }
                    }
                }
            })
        }

        poll()
    }

    fun postRepoDocumentation(repoUrl: String, pat: String, branch: String) {
        val json = JSONObject()
        json.put("repo_url", repoUrl)
        json.put("branch", branch)
        json.put("pat", pat)

        val requestBody = json.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$storedUrl/assistant/repo-documentation")
            .post(requestBody)
            .addHeader("Authorization", "Bearer $authToken")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Request failed: ${e.message}")
                SwingUtilities.invokeLater {
                    statusLabel.text = "Request failed: ${e.message}"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val responseBody = it.body?.string()
                    println("Initial API Response: $responseBody")

                    SwingUtilities.invokeLater {
                        // Clear markdown area while processing
                        markdownArea.text = ""
                        scrollPane.isVisible = false
                        statusLabel.text = "Initial submission successful. Waiting for job to complete..."
                    }

                    try {
                        val jsonResp = JSONObject(responseBody ?: "{}")
                        val jobID = jsonResp.getString("JobID")
                        println("Received Job ID: $jobID")
                        pollJobStatusUntilCompleted(jobID)
                    } catch (e: Exception) {
                        println("Failed to parse JobID: ${e.message}")
                        SwingUtilities.invokeLater {
                            statusLabel.text = "Failed to parse Job ID from response."
                        }
                    }
                }
            }
        })
    }

    submitButton.addActionListener {
        val repo = repoField.text.trim()
        val pat = patField.text.trim()
        val branch = branchField.text.trim()

        if (repo.isEmpty() || branch.isEmpty()) {
            statusLabel.text = "Please fill in all required fields."
            return@addActionListener
        }

        statusLabel.text = "Submitting..."
        scrollPane.isVisible = false

        postRepoDocumentation(repo, pat, branch)
    }

    cancelButton.addActionListener {
        statusLabel.text = "Cancelled."
        scrollPane.isVisible = false
        onReject()
    }

    return mainPanel
}
