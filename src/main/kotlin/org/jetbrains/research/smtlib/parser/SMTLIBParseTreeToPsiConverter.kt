package org.jetbrains.research.smtlib.parser

import com.intellij.lang.Language
import com.intellij.lang.PsiBuilder
import com.intellij.openapi.progress.ProgressIndicatorProvider
import org.antlr.intellij.adaptor.parser.ANTLRParseTreeToPSIConverter
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.ParserRuleContext

class SMTLIBParseTreeToPsiConverter(language: Language, parser: Parser, builder: PsiBuilder)
    : ANTLRParseTreeToPSIConverter(language, parser, builder) {

    override fun enterEveryRule(ctx: ParserRuleContext) {
        ProgressIndicatorProvider.checkCanceled()
        val elementType = getRuleElementTypes()[ctx.ruleIndex]
        val marker = getBuilder().mark()
        markers.push(marker)
    }

    override fun exitEveryRule(ctx: ParserRuleContext) {
        ProgressIndicatorProvider.checkCanceled()
        val elementType = getRuleElementTypes()[ctx.ruleIndex]
        val marker = markers.pop()
        marker.done(elementType)
    }
}