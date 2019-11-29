package org.jetbrains.research.smtlib.psi

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.jetbrains.research.smtlib.SmtLibParserDefinition

class Command(node: ASTNode) : ANTLRPsiNode(node)

class Constant(node: ASTNode) : ANTLRPsiNode(node)

class Sort(node: ASTNode) : ANTLRPsiNode(node)

class Term(node: ASTNode) : ANTLRPsiNode(node)

class FunDeclaration(node: ASTNode) : IdentifierDefSubtree(node, SmtLibParserDefinition.SYMBOL)
