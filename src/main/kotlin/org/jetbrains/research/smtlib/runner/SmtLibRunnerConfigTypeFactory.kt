package org.jetbrains.research.smtlib.runner

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfigurationModule
import com.intellij.openapi.project.Project

class SmtLibRunnerConfigTypeFactory(configurationType: SmtLibRunnerConfigType)
    : ConfigurationFactory(configurationType) {
    override fun createTemplateConfiguration(project: Project) =
            SmtLibRunnerConfig("", RunConfigurationModule(project), this)
}
