package org.jetbrains.research.smtlib

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.util.castSafelyTo
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Lexer


class SmtLibSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer() =
            ANTLRLexerAdaptor(SmtLibLanguage, SMTLIBv2Lexer(null))

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        val ttype = tokenType.castSafelyTo<TokenIElementType>()?.antlrTokenType ?: return EMPTY_KEYS
        val attrKey = when {
            ttype == SMTLIBv2Lexer.UndefinedSymbol -> ID
            ttype >= SMTLIBv2Lexer.CMD_Assert && ttype <= SMTLIBv2Lexer.CMD_SetOption -> BUILTIN_COMMAND
            ttype >= SMTLIBv2Lexer.PS_Not && ttype <= SMTLIBv2Lexer.PS_Unsat -> BUILTIN_FUNCTION
            ttype >= SMTLIBv2Lexer.GRW_Exclamation && ttype <= SMTLIBv2Lexer.GRW_String -> BUILTIN_FUNCTION
            ttype == SMTLIBv2Lexer.String -> CONSTANT
            ttype >= SMTLIBv2Lexer.Numeral && ttype <= SMTLIBv2Lexer.Decimal -> CONSTANT
            ttype == SMTLIBv2Lexer.Comment -> LINE_COMMENT
            else -> return EMPTY_KEYS
        }
        return arrayOf(attrKey)
    }

    companion object {

        val ID = createTextAttributesKey("SMTLIB_ID", DefaultLanguageHighlighterColors.IDENTIFIER)
        val BUILTIN_COMMAND = createTextAttributesKey("SMTLIB_BUILTIN_COMMAND", DefaultLanguageHighlighterColors.KEYWORD)
        val BUILTIN_FUNCTION = createTextAttributesKey("SMTLIB_BUILTIN_FUNCTION", DefaultLanguageHighlighterColors.STRING)
        val CONSTANT = createTextAttributesKey("SMTLIB_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val LINE_COMMENT = createTextAttributesKey("SMTLIB_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(SmtLibLanguage,
                    org.jetbrains.research.smtlib.grammar.SMTLIBv2Parser.tokenNames,
                    org.jetbrains.research.smtlib.grammar.SMTLIBv2Parser.ruleNames)
        }
    }
}
