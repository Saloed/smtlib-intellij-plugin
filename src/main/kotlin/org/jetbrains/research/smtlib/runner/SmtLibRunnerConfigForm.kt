package org.jetbrains.research.smtlib.runner


import com.intellij.execution.ui.CommonProgramParametersPanel
import com.intellij.execution.ui.MacroComboBoxWithBrowseButton
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import javax.swing.JComponent

open class SmtLibRunnerConfigForm : CommonProgramParametersPanel() {
    private lateinit var scriptPathBrowser: MacroComboBoxWithBrowseButton
    private lateinit var solverPathBrowser: MacroComboBoxWithBrowseButton
    private lateinit var solverPathComponent: LabeledComponent<MacroComboBoxWithBrowseButton>
    private lateinit var scriptNameComponent: LabeledComponent<MacroComboBoxWithBrowseButton>

    override fun setAnchor(anchor: JComponent?) {
        super.setAnchor(anchor)
        solverPathComponent.anchor = anchor
        scriptNameComponent.anchor = anchor
    }

    override fun setupAnchor() {
        super.setupAnchor()
        myAnchor = UIUtil.mergeComponentsWithAnchor(
                this, solverPathComponent, scriptNameComponent)
    }

    override fun addComponents() {
        val chooseSolverrDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor().apply {
            title = "Choose SmtLib compatible solver executable"
        }
        solverPathBrowser = MacroComboBoxWithBrowseButton(chooseSolverrDescriptor, project)
        solverPathComponent = LabeledComponent.create(solverPathBrowser, "Solver path:").apply {
            labelLocation = BorderLayout.WEST
        }
        val chooseScriptDescriptor = FileChooserDescriptorFactory.createSingleLocalFileDescriptor().apply {
            title = "Choose SmtLib file"
        }
        scriptPathBrowser = MacroComboBoxWithBrowseButton(chooseScriptDescriptor, project)
        scriptNameComponent = LabeledComponent.create(scriptPathBrowser, "SmtLib file:").apply {
            labelLocation = BorderLayout.WEST
        }

        add(scriptNameComponent)
        add(solverPathComponent)

        super.addComponents()
    }

    fun resetFormTo(configuration: SmtLibRunnerConfig) {
        reset(configuration)
        solverPathBrowser.text = configuration.solverPath
        scriptPathBrowser.text = configuration.scriptName
    }

    fun applySettingsTo(configuration: SmtLibRunnerConfig) {
        applyTo(configuration)
        configuration.solverPath = solverPathBrowser.text
        configuration.scriptName = scriptPathBrowser.text
    }
}

