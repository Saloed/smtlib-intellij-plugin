package org.jetbrains.research.smtlib.refactoring

import com.intellij.BundleBase
import com.intellij.openapi.project.Project
import com.intellij.psi.ElementDescriptionUtil
import com.intellij.psi.PsiElement
import com.intellij.refactoring.inline.InlineOptionsDialog
import com.intellij.usageView.UsageViewNodeTextLocation

class LetBindingInlineDialog(project: Project, element: PsiElement) : InlineOptionsDialog(project, true, element) {
    init {
        title = "Inline Let Binding";
        init()
    }

    override fun getBorderTitle(): String = "Inline"
    override fun getNameLabelText(): String = ElementDescriptionUtil.getElementDescription(myElement, UsageViewNodeTextLocation.INSTANCE);
    override fun getInlineAllText(): String = BundleBase.replaceMnemonicAmpersand("&All references and remove the expression");
    override fun getInlineThisText(): String = BundleBase.replaceMnemonicAmpersand("&This reference only and keep the expression");
    override fun isInlineThis(): Boolean = false

    override fun doAction() {
        val processor = LetBindingInlineRefactoringProcessor(project, myElement)
        invokeRefactoring(processor)
    }
}