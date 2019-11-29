package org.jetbrains.research.smtlib

import com.intellij.ide.plugins.PluginManager
import com.intellij.openapi.components.ProjectComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo

class SmtLibPluginController(private val project: Project) : ProjectComponent {

    override fun projectClosed() {
        LOG.info("projectClosed ${project.name}")
    }

    override fun projectOpened() {
        val plugin = PluginManager.getPlugin(PluginId.getId(PLUGIN_ID))
        val version = plugin?.version ?: "unknown"
        LOG.info("SmtLib Plugin version $version, Java version ${SystemInfo.JAVA_VERSION}")
    }

    override fun getComponentName() = "sample.ProjectComponent"

    companion object {
        const val PLUGIN_ID = "org.jetbrains.research.smtlib"
        val LOG = Logger.getInstance("SmtLibPluginController")
    }
}
