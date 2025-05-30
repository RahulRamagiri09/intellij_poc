package com.example.sidebarplugin.utils

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object IconUtils {
    fun load(path: String): Icon {
        return IconLoader.getIcon("/icons/$path", IconUtils::class.java)
    }
}
