package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.PsiElement

open class SmtLibFormatterDummySpacingBlock(element: PsiElement, spacingBuilder: SpacingBuilder)
    : SmtLibFormatterBlock(element, spacingBuilder) {
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        if (child1 == null) {
            return null
        }
        if (child1 is SmtLibFormatterLeafBlock || child2 is SmtLibFormatterLeafBlock) {
            return Spacing.createDependentLFSpacing(0, 1, element.textRange, true, 1)
        }
        if (child1 is SmtLibFormatterCommandBlock || child2 is SmtLibFormatterCommandBlock) {
            return Spacing.createDependentLFSpacing(0, 1, element.textRange, true, 1)
        }
        if (child1 is SmtLibFormatterTermBlock || child2 is SmtLibFormatterTermBlock) {
            return Spacing.createDependentLFSpacing(0, 1, element.textRange, true, 1)
        }
        return spacingBuilder.getSpacing(this, child1, child2)
    }
}
