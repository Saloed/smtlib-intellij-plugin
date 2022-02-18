package org.jetbrains.research.smtlib

import com.intellij.openapi.fileTypes.LanguageFileType

object SmtLibFileType : LanguageFileType(SmtLibLanguage) {
    const val FILE_EXTENSION = "smt2"
    override fun getName() = "SmtLib file"
    override fun getDescription() = "SmtLib file"
    override fun getDefaultExtension() = FILE_EXTENSION
    override fun getIcon() = Icons.SMTLIB_ICON
}
