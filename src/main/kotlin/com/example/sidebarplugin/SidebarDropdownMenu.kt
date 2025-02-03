//package com.example.sidebarplugin
//
//import javax.swing.*
//import javax.swing.ListSelectionModel
//import java.awt.*
//import com.intellij.ui.components.JBList
//
//class SidebarDropdownMenu(private val project: Project) : JPanel() {
//
//    init {
//        layout = BoxLayout(this, BoxLayout.Y_AXIS)
//        // Create and add the "Assistant" dropdown
//        add(createDropdownMenu("Assistant", AssistantItems.assistantList()))
//        // Create and add the "Review" dropdown
//        add(createDropdownMenu("Review", ReviewItems.reviewList()))
//    }
//
//    private fun createDropdownMenu(title: String, items: List<String>): JPanel {
//        val dropdownPanel = JPanel()
//        dropdownPanel.layout = BoxLayout(dropdownPanel, BoxLayout.Y_AXIS)
//
//        val toggleButton = JButton(title)
//        toggleButton.alignmentX = Component.LEFT_ALIGNMENT
//
//        val subcategoryList = JBList(items)
//        subcategoryList.selectionMode = ListSelectionModel.SINGLE_SELECTION
//        subcategoryList.visibleRowCount = 4
//        subcategoryList.addListSelectionListener { e ->
//            if (!e.valueIsAdjusting) {
//                val selectedItem = subcategoryList.selectedValue
//                if (selectedItem == "Code Overall Review") {
//                    ReviewItems.handleCodeReview(project)
//                }
//                JOptionPane.showMessageDialog(
//                    null,
//                    "You selected: $selectedItem",
//                    "$title Selected",
//                    JOptionPane.INFORMATION_MESSAGE
//                )
//            }
//        }
//
//        val scrollPane = JBScrollPane(subcategoryList)
//        scrollPane.alignmentX = Component.LEFT_ALIGNMENT
//        scrollPane.isVisible = false
//
//        toggleButton.addActionListener {
//            scrollPane.isVisible = !scrollPane.isVisible
//            dropdownPanel.revalidate()
//            dropdownPanel.repaint()
//        }
//
//        dropdownPanel.add(toggleButton)
//        dropdownPanel.add(scrollPane)
//
//        return dropdownPanel
//    }
//}
