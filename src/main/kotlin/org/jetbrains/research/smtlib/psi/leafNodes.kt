package org.jetbrains.research.smtlib.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.castSafelyTo
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.antlr.intellij.adaptor.psi.Trees
import org.jetbrains.research.smtlib.SmtLibLanguage
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Parser

sealed class SmtLibLeafElement(type: IElementType, text: CharSequence) : ANTLRPsiLeafNode(type, text)

class Identifier(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text), PsiNamedElement {
    override fun getName() = text
    override fun setName(name: String): PsiElement {
        parent ?: return this
        val newId = Trees.createLeafFromText(project, SmtLibLanguage, context, name, elementType) ?: return this
        return replace(newId)
    }

    override fun getReference() = PsiTreeUtil.findFirstParent(this) {
        val rule = it?.node?.elementType?.castSafelyTo<RuleIElementType>()
                ?: return@findFirstParent false
        rule.ruleIndex == SMTLIBv2Parser.RULE_qual_identifier
    }?.let { FunctionReference(this) }

    override fun toString(): String = "Identifier($elementType)"
}

class BuiltinCommandName(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "BuiltinCommandName($elementType)"
}

class PredefinedConstant(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "PredefinedConstant($elementType)"
}

class CommandName(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "CommandName($elementType)"
}

class Keyword(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "Keyword($elementType)"
}

class Number(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "Number($elementType)"
}

class Delimiter(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "Delimiter($elementType)"
}

open class Parenthesis(type: IElementType, text: CharSequence) : SmtLibLeafElement(type, text) {
    override fun toString(): String = "Par($elementType)"
}

class OpenPar(type: IElementType, text: CharSequence) : Parenthesis(type, text)
class ClosePar(type: IElementType, text: CharSequence) : Parenthesis(type, text)
