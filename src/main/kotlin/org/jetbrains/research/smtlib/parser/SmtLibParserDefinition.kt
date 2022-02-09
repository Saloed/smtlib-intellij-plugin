package org.jetbrains.research.smtlib.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.intellij.util.castSafelyTo
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.parser.ANTLRParseTreeToPSIConverter
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.v4.runtime.Parser
import org.jetbrains.research.smtlib.SmtLibLanguage
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Lexer
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Parser
import org.jetbrains.research.smtlib.grammar.SMTLIBv2Parser.*
import org.jetbrains.research.smtlib.psi.*
import org.jetbrains.research.smtlib.psi.Number

class SmtLibParserDefinition : DefaultASTFactoryImpl(), ParserDefinition {

    override fun createLexer(project: Project) = ANTLRLexerAdaptor(SmtLibLanguage, SMTLIBv2Lexer(null))

    override fun createParser(project: Project) = object : ANTLRParserAdaptor(SmtLibLanguage, SMTLIBv2Parser(null)) {
        override fun parse(parser: Parser, root: IElementType) = when (root) {
            is IFileElementType -> (parser as SMTLIBv2Parser).start()
            else -> (parser as SMTLIBv2Parser).simpleSymbol()
        }

        override fun createListener(parser: Parser, root: IElementType, builder: PsiBuilder): ANTLRParseTreeToPSIConverter {
            return SMTLIBParseTreeToPsiConverter(language, parser, builder)
        }
    }

    override fun getWhitespaceTokens(): TokenSet = PSIElementTypeFactory.createTokenSet(SmtLibLanguage, SMTLIBv2Lexer.WS)

    override fun getCommentTokens(): TokenSet = PSIElementTypeFactory.createTokenSet(SmtLibLanguage, SMTLIBv2Lexer.Comment)

    override fun getStringLiteralElements(): TokenSet = PSIElementTypeFactory.createTokenSet(SmtLibLanguage, SMTLIBv2Lexer.String)

    override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?) = ParserDefinition.SpaceRequirements.MAY

    override fun getFileNodeType() = IFileElementType(SmtLibLanguage)

    override fun createFile(viewProvider: FileViewProvider) = SmtLibFileRoot(viewProvider)

    override fun createLeaf(type: IElementType, text: CharSequence) = when (type.lexerType()) {
        LexerType.IDENTIFIER -> Identifier(type, text)
        LexerType.BUILTIN_COMMAND -> BuiltinCommandName(type, text)
        LexerType.COMMAND -> CommandName(type, text)
        LexerType.CONSTANT -> PredefinedConstant(type, text)
        LexerType.KEYWORD -> Keyword(type, text)
        LexerType.NUMBER -> Number(type, text)
        LexerType.OPEN_PAR -> OpenPar(type, text)
        LexerType.CLOSE_PAR -> ClosePar(type, text)
        LexerType.DELIMITER -> Delimiter(type, text)
        LexerType.OTHER -> super.createLeaf(type, text)
    }

    override fun createElement(node: ASTNode): PsiElement {
        val ruleIndex = node.elementType.castSafelyTo<RuleIElementType>()?.ruleIndex ?: return OtherPsiNode(node)
        return when (ruleIndex) {
            RULE_keyword -> Constant(node)
            RULE_script -> CommandList(node)
            RULE_comand_cmd_declareFun -> FunDeclaration(node)
            RULE_comand_cmd_declareConst -> FunDeclaration(node)
            RULE_comand_cmd_defineFun -> FunDefinition(node)
            RULE_sort -> Sort(node)
            RULE_sort_list -> SortList(node)
            RULE_sorted_var -> SortedVar(node)
            RULE_sorted_var_list -> SortedVarList(node)
            RULE_sorted_var_non_empty_list -> SortedVarList(node)
            RULE_var_binding_list -> VarBindingList(node)
            RULE_command -> Command(node)
            RULE_comand_cmd_assert -> AssertCommand(node)
            RULE_let_term -> LetTerm(node)
            RULE_var_binding -> VarBinding(node)
            RULE_forall_term -> ForallTerm(node)
            RULE_call_term -> CallTerm(node)
            RULE_call_term_arguments -> CallArguments(node)
            RULE_term -> Term(node)
            RULE_spec_constant -> Constant(node)
            else -> OtherPsiNode(node)
        }
    }

    companion object {
        val SYMBOL: IElementType

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(SmtLibLanguage, tokenNames, ruleNames)
            SYMBOL = PSIElementTypeFactory.getRuleIElementTypes(SmtLibLanguage)[RULE_symbol]
        }
    }

}
