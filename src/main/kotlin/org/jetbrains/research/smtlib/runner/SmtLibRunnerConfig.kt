package org.jetbrains.research.smtlib.runner

import com.intellij.execution.CommonProgramRunConfigurationParameters
import com.intellij.execution.Executor
import com.intellij.execution.RunManagerEx
import com.intellij.execution.configuration.AbstractRunConfiguration
import com.intellij.execution.configuration.EnvironmentVariablesComponent
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.util.ProgramParametersUtil
import com.intellij.openapi.components.PathMacroManager
import com.intellij.openapi.module.Module
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.util.DefaultJDOMExternalizer
import com.intellij.openapi.util.JDOMExternalizerUtil
import com.intellij.openapi.util.text.StringUtil
import org.jdom.Element
import java.nio.file.InvalidPathException
import java.nio.file.Paths

class SmtLibRunnerConfig(
        name: String,
        module: RunConfigurationModule,
        configurationFactory: ConfigurationFactory
) : AbstractRunConfiguration(name, module, configurationFactory),
        RunConfigurationWithSuppressedDefaultDebugAction,
        CommonProgramRunConfigurationParameters {

    private var workingDirectory: String = ""
    var solverOptions: String = ""
    var solverPath: String = ""
    var scriptName: String = ""

    override fun getValidModules(): Collection<Module> = allModules

    override fun excludeCompileBeforeLaunchOption() = false

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> =
            SmtLibRunnerConfigEditor(configurationModule.module)

    override fun getState(executor: Executor, env: ExecutionEnvironment): RunProfileState =
            SmtLibRunnerCommandLineState(this, env)

    override fun onNewConfigurationCreated() {
        RunManagerEx.getInstanceEx(project).setBeforeRunTasks(this, emptyList())
    }

    override fun checkConfiguration() {
        super.checkConfiguration()
        if (StringUtil.isEmptyOrSpaces(solverPath)) throw RuntimeConfigurationException("No solver path given.")
        if (StringUtil.isEmptyOrSpaces(scriptName)) throw RuntimeConfigurationError("Script name not given.")
        solverPath.findPath()
                ?: throw RuntimeConfigurationWarning("Solver path is invalid")
        configurationModule.module?.also {
            ProgramParametersUtil.checkWorkingDirectoryExist(this, project, it)
        }
    }

    override fun suggestedName(): String? {
        if (scriptName.isEmpty()) return null
        val fileName = scriptName.findPath()?.fileName ?: return null
        val name = "$fileName"
        val ind = name.lastIndexOf('.')
        return if (ind != -1) name.substring(0, ind) else name
    }

    private val SOLVER_OPTIONS = "SOLVER_OPTIONS"
    private val SOLVER_PATH = "SOLVER_PATH"
    private val SCRIPT_NAME = "SCRIPT_NAME"
    private val WORKING_DIRECTORY = "WORKING_DIRECTORY"
    private val PARENT_ENVS = "PARENT_ENVS"

    override fun readExternal(element: Element) {
        PathMacroManager.getInstance(project).expandPaths(element)
        super.readExternal(element)
        DefaultJDOMExternalizer.readExternal(this, element)
        readModule(element)
        EnvironmentVariablesComponent.readExternal(element, envs)
        solverOptions = element.read(SOLVER_OPTIONS)
        solverPath = element.read(SOLVER_PATH)
        workingDirectory = element.read(WORKING_DIRECTORY)
        scriptName = element.read(SCRIPT_NAME)
        isPassParentEnvs = element.read(PARENT_ENVS, default = "true").toBoolean()
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
        element.write(SOLVER_OPTIONS, solverOptions)
        element.write(SOLVER_PATH, solverPath)
        element.write(WORKING_DIRECTORY, workingDirectory)
        element.write(SCRIPT_NAME, scriptName)
        element.write(PARENT_ENVS, "$isPassParentEnvs")
        DefaultJDOMExternalizer.writeExternal(this, element)
        EnvironmentVariablesComponent.writeExternal(element, envs)
        PathMacroManager.getInstance(project).collapsePathsRecursively(element)
    }

    override fun setProgramParameters(value: String?) {
        if (value == null) return
        solverOptions = value
    }

    override fun getProgramParameters() = solverOptions
    override fun getWorkingDirectory() = workingDirectory

    override fun setWorkingDirectory(value: String?) {
        if (value == null) return
        workingDirectory = value
    }

    private fun String.findPath() = try {
        Paths.get(this)
    } catch (e: InvalidPathException) {
        null
    }

    private fun Element.write(key: String, value: String) {
        JDOMExternalizerUtil.writeField(this, key, value)
    }

    private fun Element.read(key: String, default: String = ""): String =
            JDOMExternalizerUtil.readField(this, key, default)

}
