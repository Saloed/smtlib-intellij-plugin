package org.jetbrains.research.smtlib.formatter

import com.intellij.formatting.Alignment
import com.intellij.formatting.Block
import com.intellij.formatting.Indent
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.formatter.common.AbstractBlock
import org.jetbrains.research.smtlib.psi.*

abstract class SmtLibFormatterBlock(val element: PsiElement) : AbstractBlock(element.node, null, null) {
    override fun isLeaf() = false
    abstract override fun getIndent(): Indent

    val predefinedSpacing = hashMapOf<Pair<Block?, Block>, Spacing>()
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        val predefinedSpace = predefinedSpacing[child1 to child2]
        return when {
            predefinedSpace != null -> predefinedSpace
            child1 == null -> NO_SPACING
            else -> spacing(child1, child2)
        }
    }

    var blockAlignment: Alignment? = null
    fun getOrCreateAlignment(): Alignment = when {
        blockAlignment != null -> blockAlignment!!
        else -> {
            blockAlignment = Alignment.createAlignment()
            blockAlignment!!
        }
    }

    fun findNearestNameEnding(): SmtLibFormatterBlock? = when (this) {
        is NameFormatterBlock -> this.endBlock
        else -> buildChildren()
                .mapNotNull { it.findNearestNameEnding() }
                .firstOrNull()
    }

    override fun getAlignment(): Alignment? = blockAlignment

    abstract fun spacing(child1: Block, child2: Block): Spacing?

    override fun buildChildren() = element.children
            .filter { it.node.nonEmptyBlock() }
            .flatMap { it.formatterBlock() }

    val isBig: Boolean by lazy {
        if(element.textLength < BIG_THRESHOLD) return@lazy false
        val leafs = element.collectChildrenMatchingPredicate { it is SmtLibLeafElement }
        if(leafs.all { it.textLength < BIG_THRESHOLD }) return@lazy true
        return@lazy leafs.size > 20
    }

    fun spaceParenthesis(child1: Block, child2: Block, default: Spacing) = when {
        child1 is ParenthesisFormatterBlock && child2 is ParenthesisFormatterBlock -> NO_SPACING
        child1 is ParenthesisFormatterBlock -> when (child1.isOpen) {
            true -> NO_SPACING
            false -> SINGLE_SPACE_NO_LB
        }
        child2 is ParenthesisFormatterBlock -> when (child2.isOpen) {
            true -> SINGLE_SPACE_NO_LB
            false -> NO_SPACING
        }
        else -> default
    }

    companion object {
        fun create(element: PsiElement): Block = element.formatterBlock().first { it is CommandListFormatterBlock }

        fun ASTNode.nonEmptyBlock(): Boolean = text.trim().isNotEmpty()

        fun PsiElement.formatterBlock(): List<SmtLibFormatterBlock> = when (this) {
            is OpenPar -> listOf(ParenthesisFormatterBlock(true, this))
            is ClosePar -> listOf(ParenthesisFormatterBlock(false, this))
            is SmtLibLeafElement -> listOf(NameFormatterBlock(this))
            is Command -> listOf(CommandFormatterBlock(this))
            is CommandList -> listOf(CommandListFormatterBlock(this))
            is FunDeclaration -> listOf(FunDeclarationFormatterBlock(this))
            is SortList -> listOf(SortListFormatterBlock(this))
            is Sort -> listOf(SortFormatterBlock(this))
            is SortedVarList -> listOf(SortedVarListFormatterBlock(this))
            is SortedVar -> listOf(SortedVarFormatterBlock(this))
            is CallTerm -> listOf(CallTermFormatterBlock(this))
            is CallArguments -> listOf(CallArgumentsFormatterBlock(this))
            is Constant -> listOf(ConstantFormatterBlock(this))
            is TermList -> listOf(TermListFormatterBlock(this))
            is Term -> {
                val childrenBlocks = children.flatMap { it.formatterBlock() }
                val block = when {
                    childrenBlocks.size == 1 && childrenBlocks.first() is CallTermFormatterBlock -> childrenBlocks.first()
                    else -> TermFormatterBlock(this)
                }
                listOf(block)
            }
            else -> children.flatMap { it.formatterBlock() }
        }

        data class ParenthesizedBlock(
                val open: ParenthesisFormatterBlock,
                val other: List<SmtLibFormatterBlock>,
                val close: ParenthesisFormatterBlock
        ) {

            fun alignParenthesis() {
                val alignment = open.getOrCreateAlignment()
                close.blockAlignment = alignment
            }

            companion object {
                fun fromBlocks(blocks: List<SmtLibFormatterBlock>): ParenthesizedBlock? {
                    if (blocks.size < 3) return null
                    val first = blocks.first() as? ParenthesisFormatterBlock ?: return null
                    val last = blocks.last() as? ParenthesisFormatterBlock ?: return null
                    val otherBlocks = blocks.subList(1, blocks.lastIndex)
                    return ParenthesizedBlock(first, otherBlocks, last)
                }
            }
        }

        data class CallBlocks(
                private val parBlock: ParenthesizedBlock
        ) {
            val open = parBlock.open
            val close = parBlock.close
            val name: SmtLibFormatterBlock = parBlock.other[0]
            val other: List<SmtLibFormatterBlock> = parBlock.other.drop(1)

            fun alignParenthesis() = parBlock.alignParenthesis()

            companion object {
                fun fromBlocks(blocks: List<SmtLibFormatterBlock>): CallBlocks? {
                    val parBlock = ParenthesizedBlock.fromBlocks(blocks) ?: return null
                    if (parBlock.other.isEmpty()) return null
                    val callName = parBlock.other.first()
                    if (callName is ParenthesisFormatterBlock) return null
                    return CallBlocks(parBlock)
                }
            }
        }

        const val BIG_THRESHOLD = 70

        val NO_SPACING = Spacing.createSpacing(0, 0, 0, false, 0)
        val SINGLE_SPACE_NO_LB = Spacing.createSpacing(1, 1, 0, false, 0)
        val SINGLE_SPACE_WITH_LB = Spacing.createSpacing(1, 1, 1, false, 0)

        val COMMON_SPACING_WITH_LINE_BREAKS = Spacing.createSpacing(1, 1, 0, true, 1)
        val COMMON_SPACING_WITH_SINGLE_LINE_BREAK = Spacing.createSpacing(1, 1, 1, true, 1)
        val COMMON_SPACING_WITHOUT_LINE_BREAKS = Spacing.createSpacing(1, 1, 0, false, 0)
        val COMMON_SPACING = Spacing.createSpacing(1, 1, 0, true, 1)
    }
}


class ParenthesisFormatterBlock(val isOpen: Boolean, element: PsiElement) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getNoneIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)
}

class NameFormatterBlock(val nameElement: SmtLibLeafElement) : SmtLibFormatterBlock(nameElement) {
    val startBlock = NamePartBlock(nameElement, TextRange.create(nameElement.textRange.startOffset, nameElement.textRange.startOffset))
    val allBlock = NamePartBlock(nameElement, TextRange.create(nameElement.textRange.startOffset, nameElement.textRange.endOffset))
    val endBlock = NamePartBlock(nameElement, TextRange.create(nameElement.textRange.endOffset, nameElement.textRange.endOffset))
    override fun getTextRange(): TextRange = nameElement.textRange
    override fun getIndent(): Indent = Indent.getNoneIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = null
    override fun buildChildren() = listOf<SmtLibFormatterBlock>(startBlock, allBlock, endBlock)
}

class NamePartBlock(element: PsiElement, val range: TextRange) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getNoneIndent()
    override fun getSpacing(child1: Block?, child2: Block): Spacing? = null
    override fun spacing(child1: Block, child2: Block): Spacing? = null
    override fun buildChildren() = emptyList<SmtLibFormatterBlock>()
    override fun isLeaf(): Boolean = true
    override fun getTextRange(): TextRange = range
}

class CommandFormatterBlock(element: Command) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getAbsoluteNoneIndent()
    private val isAssert: Boolean by lazy {
        element.collectChildrenMatchingPredicate(firstOnly = true) {
            it.text == "assert"
        }.isNotEmpty()
    }

    override fun buildChildren(): List<SmtLibFormatterBlock> {
        val blocks = super.buildChildren()
        if (isAssert) {
            val callBlocks = Companion.CallBlocks.fromBlocks(blocks)
            if (callBlocks != null) {
                callBlocks.alignParenthesis()
                predefinedSpacing[callBlocks.open to callBlocks.name] = NO_SPACING
                when (val nextBlock = callBlocks.other.firstOrNull()) {
                    null -> predefinedSpacing[callBlocks.name to callBlocks.close] = NO_SPACING
                    else -> predefinedSpacing[callBlocks.name to nextBlock] = SINGLE_SPACE_WITH_LB
                }
            }
        }
        return blocks
    }

    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)
}

class CommandListFormatterBlock(element: CommandList) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getAbsoluteNoneIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = COMMON_SPACING_WITH_SINGLE_LINE_BREAK
}

class FunDeclarationFormatterBlock(element: FunDeclaration) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getAbsoluteNoneIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)
}

class SortFormatterBlock(element: Sort) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)
}

class SortedVarFormatterBlock(element: SortedVar) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)
}

class TermFormatterBlock(element: Term) : SmtLibFormatterBlock(element) {
    private val isForall: Boolean by lazy {
        element.collectChildrenMatchingPredicate(firstOnly = true) {
            it.text == "forall"
        }.isNotEmpty()
    }
    private val isLet: Boolean by lazy {
        element.collectChildrenMatchingPredicate(firstOnly = true) {
            it.text == "let"
        }.isNotEmpty()
    }

    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, SINGLE_SPACE_NO_LB)

    override fun buildChildren(): List<SmtLibFormatterBlock> {
        val blocks = super.buildChildren()
        if (isForall || isLet) formatForallAndLet(blocks)
        return blocks
    }

    private fun formatForallAndLet(blocks: List<SmtLibFormatterBlock>) {
        val callBlocks = Companion.CallBlocks.fromBlocks(blocks) ?: return
        callBlocks.alignParenthesis()
        if (callBlocks.other.size != 2) return
        val bindings = callBlocks.other.first()
        val term = callBlocks.other.last()
        predefinedSpacing[callBlocks.open to callBlocks.name] = NO_SPACING
        predefinedSpacing[callBlocks.name to bindings] = SINGLE_SPACE_WITH_LB
        predefinedSpacing[bindings to term] = SINGLE_SPACE_WITH_LB
        predefinedSpacing[term to callBlocks.close] = SINGLE_SPACE_WITH_LB
        val alignment = callBlocks.name.findNearestNameEnding()?.getOrCreateAlignment() ?: return
        bindings.blockAlignment = alignment
        term.blockAlignment = alignment
    }

}

class TermListFormatterBlock(element: TermList) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = SINGLE_SPACE_WITH_LB
    override fun buildChildren(): List<SmtLibFormatterBlock> {
        val blocks = super.buildChildren()
        val parBlock = Companion.ParenthesizedBlock.fromBlocks(blocks) ?: return blocks
        parBlock.alignParenthesis()
        parBlock.other.forEach { it.blockAlignment = parBlock.open.getOrCreateAlignment() }
        return blocks
    }
}

class ConstantFormatterBlock(element: Constant) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = null
    override fun buildChildren() = mutableListOf<SmtLibFormatterBlock>()
    override fun isLeaf(): Boolean = true
}

class CallTermFormatterBlock(element: CallTerm) : SmtLibFormatterBlock(element) {

    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)

    override fun buildChildren(): List<SmtLibFormatterBlock> {
        val blocks = super.buildChildren()
        val callBlocks = Companion.CallBlocks.fromBlocks(blocks) ?: return blocks
        callBlocks.alignParenthesis()
        calculateCallSpacings(callBlocks)
        return blocks
    }

    private fun calculateCallSpacings(blocks: Companion.CallBlocks) {
        if (blocks.other.isEmpty()) return
        when {
            isBig -> {
                predefinedSpacing[blocks.open to blocks.name] = NO_SPACING
                predefinedSpacing[blocks.name to blocks.other.first()] = SINGLE_SPACE_WITH_LB
                blocks.other.zip(blocks.other.drop(1)).forEach { (left, right) ->
                    predefinedSpacing[left to right] = SINGLE_SPACE_WITH_LB
                }
                predefinedSpacing[blocks.other.last() to blocks.close] = SINGLE_SPACE_WITH_LB
                val alignment = blocks.name.findNearestNameEnding()?.getOrCreateAlignment() ?: return
                blocks.other.forEach { it.blockAlignment = alignment }
            }
            else -> {
                predefinedSpacing[blocks.open to blocks.name] = NO_SPACING
                predefinedSpacing[blocks.name to blocks.other.first()] = SINGLE_SPACE_NO_LB
                blocks.other.zip(blocks.other.drop(1)).forEach { (left, right) ->
                    predefinedSpacing[left to right] = SINGLE_SPACE_NO_LB
                }
                predefinedSpacing[blocks.other.last() to blocks.close] = NO_SPACING
            }
        }
    }
}

class CallArgumentsFormatterBlock(element: CallArguments) : SmtLibFormatterBlock(element) {
    override fun buildChildren(): List<SmtLibFormatterBlock> {
        val blocks = super.buildChildren()
        blocks.forEach { it.blockAlignment = getOrCreateAlignment() }
        return blocks
    }

    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? {
        val default = when {
            isBig -> SINGLE_SPACE_WITH_LB
            else -> SINGLE_SPACE_NO_LB
        }
        return spaceParenthesis(child1, child2, default)
    }
}

class SortListFormatterBlock(element: SortList) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITHOUT_LINE_BREAKS)
}

class SortedVarListFormatterBlock(element: SortedVarList) : SmtLibFormatterBlock(element) {
    override fun getIndent(): Indent = Indent.getContinuationIndent()
    override fun spacing(child1: Block, child2: Block): Spacing? = spaceParenthesis(child1, child2, COMMON_SPACING_WITH_SINGLE_LINE_BREAK)
    override fun buildChildren(): List<SmtLibFormatterBlock> {
        val blocks = super.buildChildren()
        val parBlock = Companion.ParenthesizedBlock.fromBlocks(blocks) ?: return blocks
        parBlock.alignParenthesis()
        if (parBlock.other.isEmpty()) return blocks
        predefinedSpacing[parBlock.open to parBlock.other.first()] = SINGLE_SPACE_WITH_LB
        predefinedSpacing[parBlock.other.last() to parBlock.close] = SINGLE_SPACE_WITH_LB
        parBlock.other.forEach { it.blockAlignment = getOrCreateAlignment() }
        return blocks
    }
}
