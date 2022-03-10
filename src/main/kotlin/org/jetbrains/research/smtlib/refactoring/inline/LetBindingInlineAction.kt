package org.jetbrains.research.smtlib.refactoring.inline

import com.intellij.lang.Language
import com.intellij.lang.refactoring.InlineActionHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.util.CommonRefactoringUtil
import org.jetbrains.research.smtlib.SmtLibLanguage
import org.jetbrains.research.smtlib.psi.Identifier
import org.jetbrains.research.smtlib.refactoring.inline.LetBindingInlineRefactoringProcessor.Companion.findLetTerm


class LetBindingInlineAction : InlineActionHandler() {
    override fun isEnabledForLanguage(l: Language?): Boolean = l == SmtLibLanguage
    override fun canInlineElement(element: PsiElement): Boolean {
        if (element !is Identifier) return false
        return findLetTerm(element) != null
    }

    override fun inlineElement(project: Project, editor: Editor?, element: PsiElement) {
        val (_, _, letTerm) = findLetTerm(element) ?: return
        val references = ReferencesSearch.search(element).findAll()
                .filter { it.element.containingFile == element.containingFile }
                .filter { it.element != element }
        if (PsiTreeUtil.hasErrorElements(letTerm)) {
            CommonRefactoringUtil.showErrorHint(project, editor, "Let Binding has errors", "Inline Let Binding", null)
            return
        }
        if (references.isEmpty()) {
            CommonRefactoringUtil.showErrorHint(project, editor, "Let Binding is never used", "Inline Let Binding ", null)
            return
        }
        val dialog = LetBindingInlineDialog(project, element)
        dialog.show()
    }
}