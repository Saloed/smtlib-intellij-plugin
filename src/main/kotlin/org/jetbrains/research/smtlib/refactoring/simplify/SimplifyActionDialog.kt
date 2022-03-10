package org.jetbrains.research.smtlib.refactoring.simplify

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.applyToComponent
import com.intellij.ui.layout.panel
import org.jetbrains.research.smtlib.formula.Simplifier
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

@OptIn(ExperimentalUnsignedTypes::class)
class SimplifyActionDialog(
    project: Project,
    private val baseParameters: List<Simplifier.SimplifierParam<*>>
) : DialogWrapper(project, true) {
    private val parameters = hashMapOf<String, Simplifier.SimplifierParam<*>>()
    val modifiedParameters: List<Simplifier.SimplifierParam<*>>
        get() = parameters.values.toList()

    init {
        init()
        title = "Simplify Expression Parameters"
    }

    override fun createCenterPanel() = JBScrollPane(panel {
        baseParameters.sortedBy { it.name }.forEach { param ->
            parameters[param.name] = param
            val row = when (param) {
                is Simplifier.BoolSimplifierParam -> row {
                    checkBox(param.name.trim(), param.default) { _, component ->
                        val current = parameters.getValue(param.name) as Simplifier.BoolSimplifierParam
                        parameters[param.name] = current.update(component.isSelected)
                    }
                }
                is Simplifier.IntSimplifierParam -> row {
                    val columns = "${UInt.MAX_VALUE}".length
                    component(JBTextField("${param.default}", columns))
                        .applyToComponent {
                            fun JBTextField.onChange() {
                                val value = text.toUIntOrNull() ?: return
                                val current = parameters.getValue(param.name) as Simplifier.IntSimplifierParam
                                parameters[param.name] = current.update(value)
                            }
                            document.addDocumentListener(object : DocumentListener {
                                override fun insertUpdate(p0: DocumentEvent?) {
                                    this@applyToComponent.onChange()
                                }

                                override fun removeUpdate(p0: DocumentEvent?) {
                                    this@applyToComponent.onChange()
                                }

                                override fun changedUpdate(p0: DocumentEvent?) {
                                    this@applyToComponent.onChange()
                                }
                            })
                        }
                    label(param.name.trim())
                }
            }
            row.commentNoWrap(param.description.trim())
        }
    })
}
