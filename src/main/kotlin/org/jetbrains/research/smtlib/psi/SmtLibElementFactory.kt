package org.jetbrains.research.smtlib.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.research.smtlib.SmtLibLanguage

object SmtLibElementFactory {
    fun createTerm(text: String, project: Project): Term {
        val file = createFile("(assert $text )", project)
        return PsiTreeUtil.findChildOfType(file, AssertCommand::class.java)?.term()
                ?: error("Error while Term for text: $text")
    }

    private fun createFile(text: String, project: Project): PsiFile = PsiFileFactory.getInstance(project).createFileFromText(SmtLibLanguage, text)
}
