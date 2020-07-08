package org.jetbrains.research.smtlib.psi

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.jetbrains.research.smtlib.parser.SmtLibParserDefinition

open class OtherPsiNode(node: ASTNode) : ANTLRPsiNode(node)

open class Command(node: ASTNode) : ANTLRPsiNode(node)

class CommandList(node: ASTNode) : ANTLRPsiNode(node)

class FunDeclaration(node: ASTNode) : IdentifierDefSubtree(node, SmtLibParserDefinition.SYMBOL)

open class Sort(node: ASTNode) : ANTLRPsiNode(node)

open class SortedVar(node: ASTNode) : ANTLRPsiNode(node)

open class Term(node: ASTNode) : ANTLRPsiNode(node)

open class TermList(node: ASTNode) : ANTLRPsiNode(node)


class Constant(node: ASTNode) : Term(node)

class CallTerm(node: ASTNode) : Term(node)

class CallArguments(node: ASTNode) : TermList(node)

class SortList(node: ASTNode) : ANTLRPsiNode(node)

class SortedVarList(node: ASTNode) : ANTLRPsiNode(node)
