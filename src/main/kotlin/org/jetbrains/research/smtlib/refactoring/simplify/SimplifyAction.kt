package org.jetbrains.research.smtlib.refactoring.simplify

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
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
        val dialog = SimplifyActionDialog(project, Simplifier.simplificationParameters())
        if (!dialog.showAndGet()) return
        val declarations = PsiTreeUtil.collectElementsOfType(file, FunctionDeclaration::class.java)
        val declarationsText = declarations.joinToString("\n") { "(${it.text})" }
        val assertWithDeclarations = "$declarationsText\n(${nearestAssert.text})"
        val simplifiedAssertions = Simplifier.simplifyAssertStatement(assertWithDeclarations, dialog.params)
        val simplifiedAssert = simplifiedAssertions.singleOrNull() ?: run {
            logger<SimplifyAction>().warn("Non single assert after simplify")
            return
        }
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

    private fun PsiElement.nearestAssert() = PsiTreeUtil.getParentOfType(this, AssertCommand::class.java, false)
    private fun AnActionEvent.nearestAssertCommand(): AssertCommand? =
        getSmtLibElement()?.nearestAssert()
            ?: findSelectedSmtLibElement()?.nearestAssert()
}
