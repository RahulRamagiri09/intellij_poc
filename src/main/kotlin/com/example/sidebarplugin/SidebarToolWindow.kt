////working with two category when click on category the list will open
//package com.example.sidebarplugin
//
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.vfs.VirtualFile
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.ui.SimpleToolWindowPanel
//import com.intellij.ui.components.JBList
//import com.intellij.ui.components.JBScrollPane
//import javax.swing.*
//import java.awt.*
//
//class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
//
//    private val mainPanel: JPanel = JPanel()
//
//    init {
//        // Set layout for the main panel
//        mainPanel.layout = BorderLayout()
//
//        // Add the dropdown menu to the main panel
//        addDropdownMenu()
//
//        // Set the main panel as the content of the tool window
//        setContent(mainPanel)
//    }
//
//    private fun addDropdownMenu() {
//        // Create the parent categories
//        val categories = listOf("Assistant", "Review")
//
//        // Create a JList for parent categories
//        val categoryList = JBList(categories)
//        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//
//        // Create a panel to hold the dropdown menus
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Create and add the "Assistant" dropdown
//        val assistantDropdown = createDropdownMenu(
//            "Assistant",
//            listOf(
//                "Assistant 1",
//                "Assistant 2",
//                "Assistant 3",
//                "Assistant 4",
//                "Assistant 5",
//                "Assistant 6",
//                "Assistant 7",
//                "Assistant 8"
//            )
//        )
//        dropdownPanel.add(assistantDropdown)
//
//        // Create and add the "Review" dropdown (currently no features added, but extendable)
//        val reviewDropdown = createDropdownMenu("Review", listOf("Ck Review", "Code Overall Review"))
//        dropdownPanel.add(reviewDropdown)
//
//        // Add the dropdown panel to the main panel inside a scroll pane
//        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
//    }
//
//    private fun createDropdownMenu(title: String, items: List<String>): JPanel {
//        // Panel to hold the dropdown menu
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Create the toggle button
//        val toggleButton = JButton(title)
//        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
//
//        // Create the list for subcategories
//        val subcategoryList = JBList(items)
//        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//        subcategoryList.visibleRowCount = 4
//        subcategoryList.addListSelectionListener { e ->
//            if (!e.valueIsAdjusting) {
//                val selectedItem = subcategoryList.selectedValue
//                if (selectedItem == "Ck Review") {
//                    // Print the access token and active file format when "Ck Review" is selected
//                    println("Access Token: ${AuthTokenStorage.accessToken}")
//                    printActiveFileFormat()
//
//                    // In your action or event handler
//                    val gitInfo = GitInfo.getGitInfo(project) // 'project' is the current Project instance
//                    println(gitInfo)
//
//
//                }
//
//                JOptionPane.showMessageDialog(
//                    null,
//                    "You selected: $selectedItem",
//                    "$title Selected",
//                    JOptionPane.INFORMATION_MESSAGE
//                )
//            }
//        }
//
//        // Wrap the list in a scroll pane
//        val scrollPane = JBScrollPane(subcategoryList)
//        scrollPane.alignmentX = Component.LEFT_ALIGNMENT
//        scrollPane.isVisible = false // Initially hide the list
//
//        // Add action listener to toggle the visibility of the list
//        toggleButton.addActionListener {
//            scrollPane.isVisible = !scrollPane.isVisible
//            dropdownPanel.revalidate()
//            dropdownPanel.repaint()
//        }
//
//        // Add components to the dropdown panel
//        dropdownPanel.add(toggleButton)
//        dropdownPanel.add(scrollPane)
//
//        return dropdownPanel
//    }
//
//    private fun printActiveFileFormat() {
//        // Get the currently active editor file in the project
//        val editor = project?.let { FileEditorManager.getInstance(it).selectedTextEditor }
//        val activeFile = editor?.virtualFile
//        val fileExtension = activeFile?.extension
//
//        // Print the file format (extension)
//        println("Active file format: $fileExtension")
//    }
//}
//
////val gitInfoFetcher = GitInfoFetcher()
////val gitInfo = gitInfoFetcher.getGitInfo(project) // `project` is your IntelliJ project object
////println("Project Name: ${gitInfo.projectName}, Branch Name: ${gitInfo.branchName}")
//
//
//


////working with all parameters except selected code
//package com.example.sidebarplugin
//
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.vfs.VirtualFile
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.ui.SimpleToolWindowPanel
//import com.intellij.ui.components.JBList
//import com.intellij.ui.components.JBScrollPane
//import javax.swing.*
//import java.awt.*
//import com.example.sidebarplugin.GitInfo
//
//
//class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
//
//    private val mainPanel: JPanel = JPanel()
//
//    init {
//        // Set layout for the main panel
//        mainPanel.layout = BorderLayout()
//
//        // Add the dropdown menu to the main panel
//        addDropdownMenu()
//
//        // Set the main panel as the content of the tool window
//        setContent(mainPanel)
//    }
//
//    private fun addDropdownMenu() {
//        // Create the parent categories
//        val categories = listOf("Assistant", "Review")
//
//        // Create a JList for parent categories
//        val categoryList = JBList(categories)
//        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//
//        // Create a panel to hold the dropdown menus
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Create and add the "Assistant" dropdown
//        val assistantDropdown = createDropdownMenu(
//            "Assistant",
//            listOf(
//                "Assistant 1",
//                "Assistant 2",
//                "Assistant 3",
//                "Assistant 4",
//                "Assistant 5",
//                "Assistant 6",
//                "Assistant 7",
//                "Assistant 8"
//            )
//        )
//        dropdownPanel.add(assistantDropdown)
//
//        // Create and add the "Review" dropdown (currently no features added, but extendable)
//        val reviewDropdown = createDropdownMenu("Review", listOf("Ck Review", "Code Overall Review"))
//        dropdownPanel.add(reviewDropdown)
//
//        // Add the dropdown panel to the main panel inside a scroll pane
//        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
//    }
//
//    private fun createDropdownMenu(title: String, items: List<String>): JPanel {
//        // Panel to hold the dropdown menu
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Create the toggle button
//        val toggleButton = JButton(title)
//        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
//
//        // Create the list for subcategories
//        val subcategoryList = JBList(items)
//        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//        subcategoryList.visibleRowCount = 4
//        subcategoryList.addListSelectionListener { e ->
//            if (!e.valueIsAdjusting) {
//                val selectedItem = subcategoryList.selectedValue
//                if (selectedItem == "Ck Review") {
//                    // Print the access token and active file format when "Ck Review" is selected
//                    println("Access Token: ${AuthTokenStorage.accessToken}")
//                    printActiveFileFormat()
//
//                    // In your action or event handler
//                    val gitInfo = GitInfo.getGitInfo(project) // 'project' is the current Project instance
//                    println("git ifo : $gitInfo")
//
//
//                }
//
//                JOptionPane.showMessageDialog(
//                    null,
//                    "You selected: $selectedItem",
//                    "$title Selected",
//                    JOptionPane.INFORMATION_MESSAGE
//                )
//            }
//        }
//
//        // Wrap the list in a scroll pane
//        val scrollPane = JBScrollPane(subcategoryList)
//        scrollPane.alignmentX = Component.LEFT_ALIGNMENT
//        scrollPane.isVisible = false // Initially hide the list
//
//        // Add action listener to toggle the visibility of the list
//        toggleButton.addActionListener {
//            scrollPane.isVisible = !scrollPane.isVisible
//            dropdownPanel.revalidate()
//            dropdownPanel.repaint()
//        }
//
//        // Add components to the dropdown panel
//        dropdownPanel.add(toggleButton)
//        dropdownPanel.add(scrollPane)
//
//        return dropdownPanel
//    }
//
//    private fun printActiveFileFormat() {
//        // Get the currently active editor file in the project
//        val editor = project?.let { FileEditorManager.getInstance(it).selectedTextEditor }
//        val activeFile = editor?.virtualFile
//        val fileExtension = activeFile?.extension
//
//        // Print the file format (extension)
//        println("Active file format: $fileExtension")
//    }
//}



//working with all parameters
//package com.example.sidebarplugin
//
//import com.intellij.openapi.fileEditor.FileEditorManager
//import com.intellij.openapi.vfs.VirtualFile
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.ui.SimpleToolWindowPanel
//import com.intellij.ui.components.JBList
//import com.intellij.ui.components.JBScrollPane
//import javax.swing.*
//import java.awt.*
//
//
//
//
//class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
//    private val mainPanel: JPanel = JPanel()
//    init {
//        // Set layout for the main panel
//        mainPanel.layout = BorderLayout()
//        // Add the dropdown menu to the main panel
//        addDropdownMenu()
//        // Set the main panel as the content of the tool window
//        setContent(mainPanel)
//    }
//
//    private fun addDropdownMenu() {
//        // Create the parent categories
//        val categories = listOf("Assistant", "Review")
//
//        // Create a JList for parent categories
//        val categoryList = JBList(categories)
//        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//
//        // Create a panel to hold the dropdown menus
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Create and add the "Assistant" dropdown
//        val assistantDropdown = createDropdownMenu(
//            "Assistant",
//            listOf(
//                "Assistant 1",
//                "Assistant 2",
//                "Assistant 3",
//                "Assistant 4",
//                "Assistant 5",
//                "Assistant 6",
//                "Assistant 7",
//                "Assistant 8"
//            )
//        )
//        dropdownPanel.add(assistantDropdown)
//
//        // Create and add the "Review" dropdown (currently no features added, but extendable)
//        val reviewDropdown = createDropdownMenu("Review", listOf("Ck Review", "Code Overall Review"))
//        dropdownPanel.add(reviewDropdown)
//
//        // Add the dropdown panel to the main panel inside a scroll pane
//        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
//    }
//
//    private fun createDropdownMenu(title: String, items: List<String>): JPanel {
//        // Panel to hold the dropdown menu
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        // Create the toggle button
//        val toggleButton = JButton(title)
//        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
//
//        // Create the list for subcategories
//        val subcategoryList = JBList(items)
//        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//        subcategoryList.visibleRowCount = 4
//        subcategoryList.addListSelectionListener { e ->
//            if (!e.valueIsAdjusting) {
//                val selectedItem = subcategoryList.selectedValue
//                if (selectedItem == "Code Overall Review") {
////                    val authToken = AuthTokenStorage.accessToken
//                    val authToken = AuthTokenStorage.accessToken ?: ""
//
//
//                    val activeFile = project.let { FileEditorManager.getInstance(it).selectedTextEditor?.virtualFile }
//                    val fileExtension = activeFile?.extension ?: "NA"
//
//                    // Map file extensions to full language names
//                    val language = when (fileExtension.lowercase()) {
//                        "py" -> "Python"
//                        "java" -> "Java"
//                        "kt" -> "Kotlin"
//                        "js" -> "JavaScript"
//                        "ts" -> "TypeScript"
//                        "cpp", "cxx", "cc" -> "C++"
//                        "c" -> "C"
//                        "cs" -> "C#"
//                        "rb" -> "Ruby"
//                        "php" -> "PHP"
//                        "swift" -> "Swift"
//                        "go" -> "Go"
//                        "rs" -> "Rust"
//                        "html" -> "HTML"
//                        "css" -> "CSS"
//                        "sh" -> "Shell Script"
//                        "json" -> "JSON"
//                        "yaml", "yml" -> "YAML"
//                        "xml" -> "XML"
//                        "sql" -> "SQL"
//                        else -> "Unknown"
//                    }
//
//                    println("Detected language: $language")
//
//                    val gitInfo = GitInfo.getGitInfo(project)
//
//                    val projectName = gitInfo?.repositoryName ?: "NA"
//                    val branchName = gitInfo?.currentBranch ?: "NA"
//                    val editor = project.let { FileEditorManager.getInstance(it).selectedTextEditor }
//                    val selectedCode = editor?.selectionModel?.selectedText ?: "No Code Selected"
//
//
//                    println("Access Token: $authToken")
//                    println("Active file format: $language")
//                    println("Git Info: $projectName, $branchName")
//                    println("selected code : $selectedCode")
//
//                    // Make the API call asynchronously
////                    sendReviewRequest(code, fileExtension, projectName, branchName, authToken)
//
//                    sendReviewRequest(
//                        text = selectedCode,
//                        language = language,
//                        authToken,
//                        projectName = projectName,
//                        branchName = branchName
//                    )
//
//
//                }
//
//                JOptionPane.showMessageDialog(
//                    null,
//                    "You selected: $selectedItem",
//                    "$title Selected",
//                    JOptionPane.INFORMATION_MESSAGE
//                )
//            }
//        }
//
//        // Wrap the list in a scroll pane
//        val scrollPane = JBScrollPane(subcategoryList)
//        scrollPane.alignmentX = Component.LEFT_ALIGNMENT
//        scrollPane.isVisible = false // Initially hide the list
//
//        // Add action listener to toggle the visibility of the list
//        toggleButton.addActionListener {
//            scrollPane.isVisible = !scrollPane.isVisible
//            dropdownPanel.revalidate()
//            dropdownPanel.repaint()
//        }
//
//        // Add components to the dropdown panel
//        dropdownPanel.add(toggleButton)
//        dropdownPanel.add(scrollPane)
//
//        return dropdownPanel
//    }
//
//    private fun printActiveFileFormat() {
//        // Get the currently active editor file in the project
//        val editor = project?.let { FileEditorManager.getInstance(it).selectedTextEditor }
//        val activeFile = editor?.virtualFile
//        val fileExtension = activeFile?.extension
//
//        // Print the file format (extension)
//        println("Active file format: $fileExtension")
//    }
//}



package com.example.sidebarplugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBList
import com.intellij.ui.components.JBScrollPane
import javax.swing.*
import java.awt.*

class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
    private val mainPanel: JPanel = JPanel()

    init {
        mainPanel.layout = BorderLayout()
        addProjectNameHeader()
        addDropdownMenu()
        setContent(mainPanel)
    }

    private fun addProjectNameHeader() {
        val projectNameLabel = JLabel("Project: ${project.name}")
        projectNameLabel.font = Font("Arial", Font.BOLD, 14)
        projectNameLabel.horizontalAlignment = SwingConstants.CENTER

        mainPanel.add(projectNameLabel, BorderLayout.NORTH)
    }

    private fun addDropdownMenu() {
        val categories = listOf("Assistant", "Review")
        val categoryList = JBList(categories)
        categoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        val dropdownPanel = JPanel()
        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)

        // Usage
        val assistantDropdown = DropDownMenuAssistant.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
        dropdownPanel.add(assistantDropdown)

        val reviewDropdown = DropdownMenuReview.createDropdownMenu("Review", ReviewItems.getItems(), project)
        dropdownPanel.add(reviewDropdown)

        mainPanel.add(JBScrollPane(dropdownPanel), BorderLayout.CENTER)
    }
}

//
//package com.example.sidebarplugin
//
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.ui.SimpleToolWindowPanel
//import com.intellij.ui.components.JBScrollPane
//import javax.swing.*
//import java.awt.*
//
//class SidebarToolWindow(private val project: Project) : SimpleToolWindowPanel(true, true) {
//    private val leftPanel: JPanel = JPanel()
//    private val rightPanel: JTextArea = JTextArea() // Right-side response panel
//
//    init {
//        layout = BorderLayout()
//
//        // Setup the panels
//        setupLeftPanel()
//        setupRightPanel()
//
//        // Create a split pane (left: dropdowns, right: response)
//        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, JBScrollPane(rightPanel))
//        splitPane.dividerLocation = 200 // Adjust width of left panel
//
//        add(splitPane, BorderLayout.CENTER)
//    }
//
//    private fun setupLeftPanel() {
//        leftPanel.layout = BoxLayout(leftPanel, BoxLayout.Y_AXIS)
//
//        val projectNameLabel = JLabel("Project: ${project.name}")
//        projectNameLabel.font = Font("Arial", Font.BOLD, 14)
//        projectNameLabel.alignmentX = Component.CENTER_ALIGNMENT
//        leftPanel.add(projectNameLabel)
//
//        leftPanel.add(Box.createVerticalStrut(10)) // Spacing
//
//        val assistantDropdown = DropdownMenu.createDropdownMenu("Assistant", AssistantItems.getItems(), project)
//        leftPanel.add(assistantDropdown)
//
//        leftPanel.add(Box.createVerticalStrut(10)) // Spacing
//
//        val reviewDropdown = DropdownMenu.createDropdownMenu("Review", ReviewItems.getItems(), project)
//        leftPanel.add(reviewDropdown)
//    }
//
//    private fun setupRightPanel() {
//        rightPanel.font = Font("Arial", Font.PLAIN, 14)
//        rightPanel.isEditable = false
//    }
//
//    private fun updateRightPanel(content: String) {
//        rightPanel.text = content
//    }
//}
