package org.jetbrains.research.smtlib.psi

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.jetbrains.research.smtlib.parser.SmtLibParserDefinition

abstract class SmtLibPsiNode(node: ASTNode) : ANTLRPsiNode(node), SmtLibPsiElement

open class OtherPsiNode(node: ASTNode) : SmtLibPsiNode(node)

open class Command(node: ASTNode) : SmtLibPsiNode(node)

class CommandList(node: ASTNode) : SmtLibPsiNode(node)

class FunDeclaration(node: ASTNode) : IdentifierDefSubtree(node, SmtLibParserDefinition.SYMBOL) {
    fun funIdentifier(): Identifier = PsiTreeUtil.findChildOfType(children[1], Identifier::class.java)
            ?: throw IllegalStateException("Function has no identifier")
}

open class Sort(node: ASTNode) : SmtLibPsiNode(node)

open class SortedVar(node: ASTNode) : SmtLibPsiNode(node) {
    fun varIdentifier(): Identifier = PsiTreeUtil.findChildOfType(this, Identifier::class.java)
            ?: throw IllegalStateException("Sorted var has no identifier")
}

open class Term(node: ASTNode) : SmtLibPsiNode(node)

open class TermList(node: ASTNode) : SmtLibPsiNode(node)

class Constant(node: ASTNode) : Term(node)

class CallTerm(node: ASTNode) : Term(node)

class CallArguments(node: ASTNode) : TermList(node)

class SortList(node: ASTNode) : SmtLibPsiNode(node)

class SortedVarList(node: ASTNode) : SmtLibPsiNode(node)

// todo: this nodes are not Other but formatter ...
class AssertCommand(node: ASTNode) : OtherPsiNode(node)
class LetTerm(node: ASTNode) : OtherPsiNode(node) {
    fun declarations(): List<Identifier> {
        val varBindingList = PsiTreeUtil.getChildOfType(this, TermList::class.java) ?: return emptyList()
        val varBindings = PsiTreeUtil.getChildrenOfType(varBindingList, VarBinding::class.java) ?: return emptyList()
        return varBindings.filterNotNull().map { it.varIdentifier() }
    }
}

class ForallTerm(node: ASTNode) : OtherPsiNode(node) {
    fun declarations(): List<Identifier> {
        val varDeclList = PsiTreeUtil.getChildOfType(this, SortedVarList::class.java) ?: return emptyList()
        val varDecls = PsiTreeUtil.getChildrenOfType(varDeclList, SortedVar::class.java) ?: return emptyList()
        return varDecls.filterNotNull().map { it.varIdentifier() }
    }
}

class VarBinding(node: ASTNode) : Term(node) {
    fun varIdentifier(): Identifier = PsiTreeUtil.findChildOfType(this, Identifier::class.java)
            ?: throw IllegalStateException("Var binding has no identifier")
}
