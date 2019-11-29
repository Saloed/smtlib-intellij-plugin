package org.jetbrains.research.smtlib

import com.intellij.core.CoreASTFactory
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Lexer
import org.jetbrains.research.smtlib.psi.Identifier

class SmtLibASTFactory : CoreASTFactory() {
    override fun createLeaf(type: IElementType, text: CharSequence) = when {
        type is TokenIElementType && type.antlrTokenType == SMTLIBv2Lexer.UndefinedSymbol -> Identifier(type, text)
        else -> super.createLeaf(type, text)
    }
}
