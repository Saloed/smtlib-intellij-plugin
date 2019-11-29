package org.jetbrains.research.smtlib.runner

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.PossiblyDumbAware
import org.jetbrains.research.smtlib.Icons

class SmtLibRunnerConfigType :
        ConfigurationTypeBase("SmtLibRunConfigurationType", "SmtLib", "SmtLib run configuration", Icons.SMTLIB_ICON),
        PossiblyDumbAware {
    init {
        addFactory(SmtLibRunnerConfigTypeFactory(this))
    }

    override fun isDumbAware() = true
}
