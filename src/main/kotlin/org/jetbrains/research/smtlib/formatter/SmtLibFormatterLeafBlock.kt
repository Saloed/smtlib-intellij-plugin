package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.PsiElement

class SmtLibFormatterLeafBlock(element: PsiElement, spacingBuilder: SpacingBuilder)
    : SmtLibFormatterBlock(element, spacingBuilder) {
    override fun isLeaf() = true
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
    override fun buildChildren() = mutableListOf<Block>()
}
