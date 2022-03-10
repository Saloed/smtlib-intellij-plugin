package org.jetbrains.research.smtlib.refactoring.simplify

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.runUndoTransparentWriteAction
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.smtlib.formula.Simplifier
import org.jetbrains.research.smtlib.psi.*


internal fun AnActionEvent.getSmtLibElement() = getData(CommonDataKeys.PSI_ELEMENT) as? SmtLibPsiElement
internal fun AnActionEvent.nearestAssertCommand(): AssertCommand? =
    getSmtLibElement()?.nearestAssert()
        ?: findSelectedSmtLibElement()?.nearestAssert()

internal fun AnActionEvent.findSelectedSmtLibElement(): SmtLibPsiElement? {
    val caret = getData(CommonDataKeys.CARET) ?: return null
    val file = getData(CommonDataKeys.PSI_FILE) as? SmtLibFileRoot ?: return null
    val selectedElement = file.findElementAt(caret.offset) ?: return null
    return PsiTreeUtil.getParentOfType(selectedElement, SmtLibPsiElement::class.java, false)
}

internal fun PsiElement.nearestAssert() = PsiTreeUtil.getParentOfType(this, AssertCommand::class.java, false)

internal fun performSimplify(
    project: Project,
    file: PsiElement,
    nearestAssert: AssertCommand,
    params: List<Simplifier.SimplifierParam<*>>? = null
) {
    val declarations = PsiTreeUtil.collectElementsOfType(file, FunctionDeclaration::class.java)
    val declarationsText = declarations.joinToString("\n") { "(${it.text})" }
    val assertWithDeclarations = "$declarationsText\n(${nearestAssert.text})"
    val simplifiedAssertions = Simplifier.simplifyAssertStatement(assertWithDeclarations, params)
    val simplifiedAssert = simplifiedAssertions.singleOrNull() ?: run {
        logger<SimplifyAction>().warn("Non single assert after simplify")
        return
    }
    val simplifiedAssertElement = SmtLibElementFactory.createAssert(simplifiedAssert, project)
    runUndoTransparentWriteAction {
        nearestAssert.replace(simplifiedAssertElement)
    }
}

