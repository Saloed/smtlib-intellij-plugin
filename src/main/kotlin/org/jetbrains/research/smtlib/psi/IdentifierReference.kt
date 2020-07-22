package org.jetbrains.research.smtlib.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.*
import com.intellij.psi.util.PsiTreeUtil


class IdentifierReference(element: Identifier) : PsiReferenceBase<Identifier>(element, TextRange(0, element.text.length)) {

    override fun resolve(): PsiElement? {
        val parentNodes = PsiTreeUtil.collectParents(element, PsiElement::class.java, false) { false }
        var declaration: PsiElement? = null
        for (parent in parentNodes) {
            if (declaration != null) break
            declaration = findElementDeclaration(parent)
        }
        return declaration
    }

    private fun findElementDeclaration(declarationProvider: PsiElement): PsiElement? = when (declarationProvider) {
        is LetTerm -> findDeclaration(declarationProvider)
        is ForallTerm -> findDeclaration(declarationProvider)
        is PsiFile -> findDeclaration(declarationProvider)
        else -> null
    }

    private fun findDeclaration(declarationProvider: LetTerm): PsiElement? =
            declarationProvider.declarations().find { it match element }


    private fun findDeclaration(declarationProvider: ForallTerm): PsiElement? =
            declarationProvider.declarations().find { it match element }

    private fun findDeclaration(declarationProvider: PsiFile): PsiElement? =
            PsiTreeUtil.findChildrenOfAnyType(declarationProvider, FunDeclaration::class.java)
                    .map { it.funIdentifier() }
                    .find { it match element }

    private infix fun Identifier.match(other: Identifier): Boolean = name == other.name

    override fun handleElementRename(newElementName: String) = element.setName(newElementName)
}
