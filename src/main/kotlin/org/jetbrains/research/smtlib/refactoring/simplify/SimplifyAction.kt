package org.jetbrains.research.smtlib.refactoring.simplify

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import org.jetbrains.research.smtlib.formula.Simplifier
import org.jetbrains.research.smtlib.psi.*

class SimplifyAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val smtLibFile = e.getData(CommonDataKeys.PSI_FILE) as? SmtLibFileRoot
        e.presentation.isEnabledAndVisible = smtLibFile != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        val nearestAssert = e.nearestAssertCommand() ?: return
        val file = nearestAssert.containingFile ?: return
        val dialog = SimplifyActionDialog(project, Simplifier.simplificationParameters)
        if (!dialog.showAndGet()) return
        performSimplify(project, file, nearestAssert, dialog.modifiedParameters)
    }
}
