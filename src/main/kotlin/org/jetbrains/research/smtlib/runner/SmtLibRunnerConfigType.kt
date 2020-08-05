package org.jetbrains.research.smtlib.runner

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationModule
import com.intellij.execution.configurations.SimpleConfigurationType
import com.intellij.openapi.util.NotNullLazyValue.createConstantValue
import com.intellij.openapi.project.Project
import org.jetbrains.research.smtlib.Icons

class SmtLibRunnerConfigType : SimpleConfigurationType("SmtLibRunConfigurationType", "SmtLib", "SmtLib run configuration", createConstantValue(Icons.SMTLIB_ICON)) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
            SmtLibRunnerConfig("", RunConfigurationModule(project), this)
}
