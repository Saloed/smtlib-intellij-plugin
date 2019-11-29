package org.jetbrains.research.smtlib.runner

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.util.ProgramParametersUtil

internal class SmtLibRunnerCommandLineState(
        private val runConfig: SmtLibRunnerConfig,
        environment: ExecutionEnvironment)
    : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val cmd = GeneralCommandLine()
        val workingDir = ProgramParametersUtil.getWorkingDir(runConfig, runConfig.project, runConfig.configurationModule.module)
        cmd.exePath = runConfig.solverPath
        cmd.parametersList.addParametersString(runConfig.solverOptions)
        cmd.addParameter(runConfig.scriptName)
        cmd.withWorkDirectory(workingDir)
        val envType = when {
            runConfig.isPassParentEnvs -> GeneralCommandLine.ParentEnvironmentType.CONSOLE
            else -> GeneralCommandLine.ParentEnvironmentType.NONE
        }
        cmd.withParentEnvironmentType(envType)
        cmd.withEnvironment(runConfig.envs)
        if (!cmd.environment.containsKey("TERM")) {
            cmd.environment["TERM"] = "xterm-256color"
        }
        val processHandler = KillableColoredProcessHandler(cmd)
        ProcessTerminatedListener.attach(processHandler, environment.project)
        return processHandler
    }

}
