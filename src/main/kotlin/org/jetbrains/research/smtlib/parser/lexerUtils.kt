package org.jetbrains.research.smtlib.parser

import com.intellij.psi.tree.IElementType
import com.intellij.util.castSafelyTo
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Lexer

enum class LexerType {
    IDENTIFIER, BUILTIN_COMMAND, COMMAND, CONSTANT, KEYWORD, NUMBER, OPEN_PAR, CLOSE_PAR, DELIMITER, OTHER;
}

fun IElementType.lexerType(): LexerType {
    val ttype = castSafelyTo<TokenIElementType>()?.antlrTokenType ?: return LexerType.OTHER
    return when (ttype) {
        SMTLIBv2Lexer.UndefinedSymbol -> LexerType.IDENTIFIER
        in SMTLIBv2Lexer.PK_AllStatistics..SMTLIBv2Lexer.PK_Version -> LexerType.BUILTIN_COMMAND
        in SMTLIBv2Lexer.PS_Not..SMTLIBv2Lexer.PS_Unsat -> LexerType.CONSTANT
        in SMTLIBv2Lexer.CMD_Assert..SMTLIBv2Lexer.CMD_SetOption -> LexerType.COMMAND
        in SMTLIBv2Lexer.GRW_Exclamation..SMTLIBv2Lexer.GRW_String -> LexerType.KEYWORD
        in SMTLIBv2Lexer.Numeral..SMTLIBv2Lexer.Decimal -> LexerType.NUMBER
        SMTLIBv2Lexer.ParOpen -> LexerType.OPEN_PAR
        SMTLIBv2Lexer.ParClose -> LexerType.CLOSE_PAR
        SMTLIBv2Lexer.Colon -> LexerType.DELIMITER
        SMTLIBv2Lexer.Semicolon -> LexerType.DELIMITER
        else -> LexerType.OTHER
    }
}
