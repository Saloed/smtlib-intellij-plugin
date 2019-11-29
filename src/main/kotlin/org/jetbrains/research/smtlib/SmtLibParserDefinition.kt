package org.jetbrains.research.smtlib

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Lexer
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Parser
import org.jetbrains.research.smtlib.psi.*

class SmtLibParserDefinition : ParserDefinition {

    override fun createLexer(project: Project) =
            ANTLRLexerAdaptor(SmtLibLanguage, SMTLIBv2Lexer(null))

    override fun createParser(project: Project) = object : ANTLRParserAdaptor(SmtLibLanguage, SMTLIBv2Parser(null)) {
        override fun parse(parser: Parser, root: IElementType) = when (root) {
            is IFileElementType -> (parser as SMTLIBv2Parser).start()
            else -> (parser as SMTLIBv2Parser).simpleSymbol()
        }
    }

    override fun getWhitespaceTokens() = WHITESPACE

    override fun getCommentTokens() = COMMENTS

    override fun getStringLiteralElements() = STRING

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?) =
            ParserDefinition.SpaceRequirements.MAY

    override fun getFileNodeType() = FILE

    override fun createFile(viewProvider: FileViewProvider) = SmtLibFileRoot(viewProvider)


    override fun createElement(node: ASTNode) = when (val elType = node.elementType) {
        is TokenIElementType -> ANTLRPsiNode(node)
        !is RuleIElementType -> ANTLRPsiNode(node)
        else -> when (elType.ruleIndex) {
            SMTLIBv2Parser.RULE_spec_constant -> Constant(node)
            SMTLIBv2Parser.RULE_sort -> Sort(node)
            SMTLIBv2Parser.RULE_command -> when {
                node.findChildByType(FUN_DECLARATION) != null -> FunDeclaration(node)
                else -> Command(node)
            }
            SMTLIBv2Parser.RULE_term -> Term(node)
            else -> ANTLRPsiNode(node)
        }
    }

    companion object {

        val FILE: IFileElementType
        val COMMENTS: TokenSet
        val WHITESPACE: TokenSet
        val STRING: TokenSet
        val ID: IElementType
        val SYMBOL: IElementType
        val FUN_DECLARATION: IElementType

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(SmtLibLanguage, SMTLIBv2Parser.tokenNames, SMTLIBv2Parser.ruleNames)
            FILE = IFileElementType(SmtLibLanguage)
            COMMENTS = PSIElementTypeFactory.createTokenSet(SmtLibLanguage, SMTLIBv2Lexer.Comment)
            WHITESPACE = PSIElementTypeFactory.createTokenSet(SmtLibLanguage, SMTLIBv2Lexer.WS)
            STRING = PSIElementTypeFactory.createTokenSet(SmtLibLanguage, SMTLIBv2Lexer.String)
            val rules = PSIElementTypeFactory.getRuleIElementTypes(SmtLibLanguage)
            ID = rules[SMTLIBv2Parser.RULE_identifier]
            SYMBOL = rules[SMTLIBv2Parser.RULE_symbol]
            FUN_DECLARATION = rules[SMTLIBv2Parser.RULE_cmd_declareFun]
        }
    }

}
