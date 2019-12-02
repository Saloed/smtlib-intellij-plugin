package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.PsiElement

class SmtLibFormatterSkipBlock(element: PsiElement, spacingBuilder: SpacingBuilder)
    : SmtLibFormatterDummySpacingBlock(element, spacingBuilder)
