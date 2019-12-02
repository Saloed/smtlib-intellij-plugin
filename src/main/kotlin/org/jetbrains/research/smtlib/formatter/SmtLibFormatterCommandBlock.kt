package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.PsiElement

class SmtLibFormatterCommandBlock(element: PsiElement, spacingBuilder: SpacingBuilder)
    : SmtLibFormatterDummySpacingBlock(element, spacingBuilder)
