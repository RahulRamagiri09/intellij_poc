package com.example.sidebarplugin.Assistant

data class AssistantItem(val label: String, val iconName: String)

object AssistantItems {
    fun getItems(): List<AssistantItem> {
        return listOf(
            AssistantItem("Add Docstring", "Docstring.svg"),
            AssistantItem("Add Error Handler", "Error_handler.svg"),
            AssistantItem("Add Logging", "Logging.svg"),
            AssistantItem("Code Generation", "Code_generation.svg"),
            AssistantItem("Comment Code", "Comment_code.svg"),
            AssistantItem("Explain Code", "Explain_code.svg"),
            AssistantItem("Refactor Code", "Refactor_code.svg"),
            AssistantItem("Unit Test Code", "Unit_test.svg"),
            AssistantItem("Filewise Unit Test Code", "Filewise_unit_test.svg")
        )
    }
}
