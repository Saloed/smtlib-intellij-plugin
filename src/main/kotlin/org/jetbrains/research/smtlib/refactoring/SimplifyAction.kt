package org.jetbrains.research.smtlib.refactoring

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.smtlib.formula.Simplifier
import org.jetbrains.research.smtlib.psi.AssertCommand
import org.jetbrains.research.smtlib.psi.SmtLibElementFactory
import org.jetbrains.research.smtlib.psi.SmtLibFileRoot
import org.jetbrains.research.smtlib.psi.SmtLibPsiElement

class SimplifyAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val smtLibFile = e.getData(CommonDataKeys.PSI_FILE) as? SmtLibFileRoot
        e.presentation.isEnabledAndVisible = smtLibFile != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.getRequiredData(CommonDataKeys.PROJECT)
        val element = e.getSmtLibElement() ?: e.findSelectedSmtLibElement() ?: return
        val file = element.containingFile ?: return
        val nearestAssert = PsiTreeUtil.getParentOfType(element, AssertCommand::class.java, false) ?: return
        val dialog = SimplifyActionDialog(project, Simplifier.simplificationParameters())
        if (!dialog.showAndGet()) return
        val allAsserts = PsiTreeUtil.findChildrenOfType(file, AssertCommand::class.java)
        val targetAssertIdx = allAsserts.indexOf(nearestAssert)
        val simplifiedAssert = Simplifier.simplifyAssertStatement(file.text, targetAssertIdx, dialog.params)
        val simplifiedAssertElement = SmtLibElementFactory.createAssert(simplifiedAssert, project)
        runUndoTransparentWriteAction {
            nearestAssert.replace(simplifiedAssertElement)
        }
    }

    private fun AnActionEvent.getSmtLibElement() = getData(CommonDataKeys.PSI_ELEMENT) as? SmtLibPsiElement
    private fun AnActionEvent.findSelectedSmtLibElement(): SmtLibPsiElement? {
        val caret = getData(CommonDataKeys.CARET) ?: return null
        val file = getData(CommonDataKeys.PSI_FILE) as? SmtLibFileRoot ?: return null
        val selectedElement = file.findElementAt(caret.offset) ?: return null
        return PsiTreeUtil.getParentOfType(selectedElement, SmtLibPsiElement::class.java, false)
    }
}
