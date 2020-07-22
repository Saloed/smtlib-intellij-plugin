package org.jetbrains.research.smtlib.parser

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.jetbrains.research.smtlib.SmtLibLanguage

class SmtLibBraceMatcher : PairedBraceMatcher {
    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset
    override fun getPairs(): Array<BracePair> = arrayOf(BracePair(
            PSIElementTypeFactory.getTokenIElementTypes(SmtLibLanguage).first { it.lexerType() == LexerType.OPEN_PAR },
            PSIElementTypeFactory.getTokenIElementTypes(SmtLibLanguage).first { it.lexerType() == LexerType.CLOSE_PAR },
            true
    ))

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true
}