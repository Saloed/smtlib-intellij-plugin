package org.jetbrains.research.smtlib.refactoring

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.jetbrains.research.smtlib.psi.Identifier

class SmtLibFindUsagesProvider : FindUsagesProvider {
    override fun getWordsScanner(): WordsScanner? = null

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String = when (element) {
        is Identifier -> element.name
        else -> ""
    }

    override fun getDescriptiveName(element: PsiElement): String = when (element) {
        is Identifier -> element.name
        else -> ""
    }

    override fun getType(element: PsiElement): String = when (element) {
        is Identifier -> "identifier"
        else -> ""
    }

    override fun getHelpId(psiElement: PsiElement): String? = null
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean = psiElement is Identifier
}