package org.jetbrains.research.smtlib

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage

class SmtLibColorSettingsPage : ColorSettingsPage {

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null

    override fun getIcon() = Icons.SMTLIB_ICON

    override fun getHighlighter() = SmtLibSyntaxHighlighter()

    override fun getDemoText() = """
        (assert (= 0 true))
        """.trimIndent()

    override fun getAttributeDescriptors() = DESCRIPTORS

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName() = "SmtLib"

    companion object {
        private val DESCRIPTORS = arrayOf(
                AttributesDescriptor("Identifier", SmtLibSyntaxHighlighter.ID),
                AttributesDescriptor("Builtin command", SmtLibSyntaxHighlighter.BUILTIN_COMMAND),
                AttributesDescriptor("Builtin function", SmtLibSyntaxHighlighter.BUILTIN_FUNCTION),
                AttributesDescriptor("Constant", SmtLibSyntaxHighlighter.CONSTANT),
                AttributesDescriptor("Line comment", SmtLibSyntaxHighlighter.LINE_COMMENT)
        )
    }
}
