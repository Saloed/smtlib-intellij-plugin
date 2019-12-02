package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.*
import com.intellij.psi.PsiElement
import com.intellij.psi.formatter.common.AbstractBlock
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.research.smtlib.psi.*

open class SmtLibFormatterBlock(val element: PsiElement, val spacingBuilder: SpacingBuilder)
    : AbstractBlock(element.node, Wrap.createWrap(WrapType.NORMAL, false), Alignment.createAlignment()) {
    override fun isLeaf() =
            element is Identifier
                    || element is Sort
                    || element is FunDeclaration
                    || element is Constant

    override fun getSpacing(child1: Block?, child2: Block): Spacing? =
            spacingBuilder.getSpacing(this, child1, child2)

    override fun buildChildren() = element.children
            .mapNotNull { buildBlocksForKnownChildren(it) }
            .toMutableList()

    private fun buildBlocksForKnownChildren(element: PsiElement): Block? = when (element) {
        is Identifier -> SmtLibFormatterLeafBlock(element, spacingBuilder)
        is Sort -> SmtLibFormatterLeafBlock(element, spacingBuilder)
        is Constant -> SmtLibFormatterLeafBlock(element, spacingBuilder)
        is FunDeclaration -> SmtLibFormatterLeafBlock(element, spacingBuilder)
        is Term -> SmtLibFormatterTermBlock(element, spacingBuilder)
        is Command -> SmtLibFormatterCommandBlock(element, spacingBuilder)
        is ANTLRPsiNode -> SmtLibFormatterSkipBlock(element, spacingBuilder)
        else -> SmtLibFormatterSkipBlock(element, spacingBuilder)
    }
}
