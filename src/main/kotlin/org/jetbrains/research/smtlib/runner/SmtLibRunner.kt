package org.jetbrains.research.smtlib.runner

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.DefaultProgramRunner

class SmtLibRunner : DefaultProgramRunner() {
    override fun getRunnerId() = "SmtLibRunner"

    override fun canRun(executorId: String, profile: RunProfile) =
            DefaultRunExecutor.EXECUTOR_ID == executorId && profile is SmtLibRunnerConfig
}
