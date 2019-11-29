package org.jetbrains.research.smtlib.runner

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiElement
import org.jetbrains.research.smtlib.psi.SmtLibFileRoot

class SmtLibRunnerConfigProducer : LazyRunConfigurationProducer<SmtLibRunnerConfig>() {
    override fun getConfigurationFactory(): ConfigurationFactory =
            SmtLibRunnerConfigTypeFactory(runConfigurationType())


    override fun setupConfigurationFromContext(
            configuration: SmtLibRunnerConfig,
            context: ConfigurationContext,
            sourceElement: Ref<PsiElement>): Boolean {
        val location = context.location ?: return false
        val psiElement = location.psiElement
        if (!psiElement.isValid) return false
        val psiFile = psiElement.containingFile as? SmtLibFileRoot ?: return false
        sourceElement.set(psiFile)
        val file = location.virtualFile ?: return false
        configuration.name = file.presentableName
        configuration.scriptName = VfsUtilCore.virtualToIoFile(file).absolutePath
        val workDir = file.parent ?: return true
        configuration.workingDirectory = VfsUtilCore.virtualToIoFile(workDir).absolutePath
        return true
    }

    override fun isConfigurationFromContext(
            configuration: SmtLibRunnerConfig,
            context: ConfigurationContext): Boolean {
        val location = context.location ?: return false
        val file = location.virtualFile ?: return false
        return FileUtil.pathsEqual(file.path, configuration.scriptName)
    }

}
