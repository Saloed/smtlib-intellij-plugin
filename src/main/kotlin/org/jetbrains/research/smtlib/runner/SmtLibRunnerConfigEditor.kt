package org.jetbrains.research.smtlib.runner

import com.intellij.openapi.module.Module
import com.intellij.openapi.options.SettingsEditor

internal class SmtLibRunnerConfigEditor(module: Module?) : SettingsEditor<SmtLibRunnerConfig>() {
    private var form: SmtLibRunnerConfigForm? = SmtLibRunnerConfigForm().apply {
        setModuleContext(module)
    }

    override fun resetEditorFrom(runConfiguration: SmtLibRunnerConfig) {
        form?.resetFormTo(runConfiguration)
    }

    override fun applyEditorTo(runConfiguration: SmtLibRunnerConfig) {
        form?.applySettingsTo(runConfiguration)
    }

    override fun createEditor() = form ?: throw IllegalStateException("Form is disposed")

    override fun disposeEditor() {
        form = null
    }
}
