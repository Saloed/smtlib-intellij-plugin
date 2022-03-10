package org.jetbrains.research.smtlib.refactoring.rename

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.jetbrains.research.smtlib.SmtLibLanguage
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Lexer
import org.jetbrains.research.smtlib.parser.LexerType
import org.jetbrains.research.smtlib.parser.lexerType

class IdentifierNameValidator : NamesValidator {
    override fun isKeyword(name: String, project: Project?): Boolean = name in literals()
    override fun isIdentifier(name: String, project: Project?): Boolean = true

    companion object {
        private fun literals() = PSIElementTypeFactory.getTokenIElementTypes(SmtLibLanguage)
                .filter { it.lexerType() in setOf(LexerType.KEYWORD, LexerType.BUILTIN_COMMAND, LexerType.COMMAND) }
                .map { SMTLIBv2Lexer.VOCABULARY.getLiteralName(it.antlrTokenType) }
                .map { it.trim('\'') }
                .toSet()
    }
}
