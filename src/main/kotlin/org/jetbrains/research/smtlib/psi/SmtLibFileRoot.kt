package org.jetbrains.research.smtlib.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.psi.FileViewProvider
import org.jetbrains.research.smtlib.Icons
import org.jetbrains.research.smtlib.SmtLibFileType
import org.jetbrains.research.smtlib.SmtLibLanguage

class SmtLibFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SmtLibLanguage) {

    override fun getFileType() = SmtLibFileType

    override fun toString() = "SmtLib Language file"

    override fun getIcon(flags: Int) = Icons.SMTLIB_ICON
}
