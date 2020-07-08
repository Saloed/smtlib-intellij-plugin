package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.util.castSafelyTo
import org.jetbrains.research.smtlib.psi.SmtLibFileRoot

class SmtLibFormatterModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val root = element.containingFile.castSafelyTo<SmtLibFileRoot>()
                ?: throw IllegalStateException("SmtLib file has no root")
        val rootBlock = SmtLibFormatterBlock.create(root)
        return FormattingModelProvider.createFormattingModelForPsiFile(root, rootBlock, settings)
    }
}
