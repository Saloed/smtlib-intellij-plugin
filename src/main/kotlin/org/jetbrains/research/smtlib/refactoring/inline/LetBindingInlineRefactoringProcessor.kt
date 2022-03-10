package org.jetbrains.research.smtlib.refactoring.inline

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.BaseRefactoringProcessor
import com.intellij.refactoring.ui.UsageViewDescriptorAdapter
import com.intellij.usageView.UsageInfo
import com.intellij.usageView.UsageViewDescriptor
import org.jetbrains.research.smtlib.psi.LetTerm
import org.jetbrains.research.smtlib.psi.VarBinding
import org.jetbrains.research.smtlib.psi.VarBindingList

class LetBindingInlineRefactoringProcessor(
        project: Project,
        val elementToInline: PsiElement
) : BaseRefactoringProcessor(project) {
    override fun getCommandName(): String = "Inline Let Binding"

    override fun findUsages(): Array<UsageInfo> =
            ReferencesSearch.search(elementToInline, elementToInline.useScope, false)
                    .map { UsageInfo(it.element) }
                    .toTypedArray()

    override fun createUsageViewDescriptor(usages: Array<out UsageInfo>): UsageViewDescriptor = LetBindingUsageViewDescriptor(elementToInline)

    override fun performRefactoring(usages: Array<out UsageInfo>) {
        val (varBinding, bindingList, letTerm) = findLetTerm(elementToInline) ?: return
        val expression = varBinding.expression()
        for (usage in usages) {
            usage.element?.replace(expression.copy())
        }
        varBinding.delete()
        val newBindings = bindingList.bindings()
        if (newBindings.isNotEmpty()) return
        letTerm.replace(letTerm.expression())
    }

    class LetBindingUsageViewDescriptor(val element: PsiElement) : UsageViewDescriptorAdapter() {
        override fun getElements(): Array<PsiElement> = arrayOf(element)
        override fun getProcessedElementsHeader(): String = "Let binding"
    }

    companion object {
        fun findLetTerm(element: PsiElement): Triple<VarBinding, VarBindingList, LetTerm>? {
            val varBinding = PsiTreeUtil.getParentOfType(element, VarBinding::class.java) ?: return null
            val bindingList = PsiTreeUtil.getParentOfType(varBinding, VarBindingList::class.java) ?: return null
            val letTerm = PsiTreeUtil.getParentOfType(bindingList, LetTerm::class.java) ?: return null
            return Triple(varBinding, bindingList, letTerm)
        }
    }
}
