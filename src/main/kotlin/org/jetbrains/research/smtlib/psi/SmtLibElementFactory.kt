package org.jetbrains.research.smtlib.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.smtlib.SmtLibLanguage

object SmtLibElementFactory {
    fun createAssert(text: String, project: Project): AssertCommand {
        val file = createFile(text, project)
        return PsiTreeUtil.findChildOfType(file, AssertCommand::class.java)
                ?: error("Error while creating Assert for text: $text")
    }

    private fun createFile(text: String, project: Project): PsiFile = PsiFileFactory.getInstance(project).createFileFromText(SmtLibLanguage, text)
}
