package org.jetbrains.research.smtlib.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor

private class ElementCollector(val firstOnly: Boolean, val predicate: (PsiElement) -> Boolean) : PsiRecursiveElementVisitor() {
    val elements = arrayListOf<PsiElement>()
    override fun visitElement(element: PsiElement) {
        if (predicate(element)) {
            elements.add(element)
            if (firstOnly) return
        }
        super.visitElement(element)
    }
}

fun PsiElement.collectChildrenMatchingPredicate(firstOnly: Boolean = false, predicate: (PsiElement) -> Boolean): List<PsiElement> {
    val collector = ElementCollector(firstOnly, predicate)
    acceptChildren(collector)
    return collector.elements
}
