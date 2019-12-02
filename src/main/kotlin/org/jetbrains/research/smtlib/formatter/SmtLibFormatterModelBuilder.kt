package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import com.intellij.util.castSafelyTo
import org.jetbrains.research.smtlib.SmtLibLanguage
import org.jetbrains.research.smtlib.SmtLibParserDefinition
import org.jetbrains.research.smtlib.psi.SmtLibFileRoot

class SmtLibFormatterModelBuilder : FormattingModelBuilder {
    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel {
        val root = element.containingFile.castSafelyTo<SmtLibFileRoot>()
                ?: throw IllegalStateException("SmtLib file has no root")
        val commandAndTerm = TokenSet.create(
                SmtLibParserDefinition.COMMAND,
                SmtLibParserDefinition.TERM
        )
        val spacingBuilder = SpacingBuilder(settings, SmtLibLanguage)
                .beforeInside(SmtLibParserDefinition.OPEN_PAR, commandAndTerm)
                .lineBreakInCode()
        val rootBlock = SmtLibFormatterBlock(root, spacingBuilder)
        return FormattingModelProvider.createFormattingModelForPsiFile(
                element.containingFile, rootBlock, settings
        )
    }
}
