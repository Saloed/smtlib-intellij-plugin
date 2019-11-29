package org.jetbrains.research.smtlib.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.PsiReferenceBase

class FunctionReference(element: Identifier) : PsiReferenceBase<Identifier>(element, TextRange(0, element.text.length)) {

    override fun handleElementRename(newElementName: String) =
            myElement.setName(newElementName)

    override fun resolve() = FunDeclarationResolver(myElement.name).apply {
        visitFile(myElement.containingFile)
    }.declaration

    internal class FunDeclarationResolver(private val identifier: String) : PsiRecursiveElementVisitor() {
        var declaration: PsiElement? = null

        override fun visitElement(element: PsiElement) {
            when {
                declaration != null -> return
                element is FunDeclaration  -> {
                    if(identifier == element.name){
                        declaration = element
                        return
                    } else {
                        return super.visitElement(element)
                    }
                }
                else -> return super.visitElement(element)
            }
        }
    }
}
