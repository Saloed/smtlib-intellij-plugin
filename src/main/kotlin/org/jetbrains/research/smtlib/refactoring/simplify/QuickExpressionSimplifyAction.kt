package org.jetbrains.research.smtlib.refactoring.simplify

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.jetbrains.research.smtlib.psi.SmtLibFileRoot

class QuickExpressionSimplifyAction : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getFamilyName(): String = "SmtLibExpressionQuickSimplify"
    override fun getText(): String = "Simplify expression"

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean =
        element.containingFile is SmtLibFileRoot && element.nearestAssert() != null

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        val nearestAssert = element.nearestAssert() ?: return
        val file = element.containingFile ?: return
        performSimplify(project, file, nearestAssert)
    }
}
