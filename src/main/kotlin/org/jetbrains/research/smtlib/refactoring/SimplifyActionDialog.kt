package org.jetbrains.research.smtlib.refactoring

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.layout.panel

class SimplifyActionDialog(project: Project, val parameters: Map<String, Pair<String, Boolean>>) : DialogWrapper(project, true) {
    val params = hashMapOf<String, Boolean>()

    init {
        init()
        title = "Simplify expression parameters"
    }

    override fun createCenterPanel() = JBScrollPane(panel {
        parameters.entries.sortedBy { it.key }.forEach { (param, info) ->
            val (documentation, default) = info
            row {
                checkBox(param.trim(), params.getOrPut(param) { default }) { _, component ->
                    params[param] = component.isSelected
                }
            }.commentNoWrap(documentation.trim())
        }
    })
}
