package org.jetbrains.research.smtlib.refactoring.rename

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import org.jetbrains.research.smtlib.psi.Identifier

class SmtLibRefactoringSupportProvider : RefactoringSupportProvider() {
    override fun isInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean = element is Identifier
    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean = element is Identifier
}
