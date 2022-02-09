package org.jetbrains.research.smtlib.refactoring

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import org.jetbrains.research.smtlib.psi.Identifier

class IdentifierManipulator : AbstractElementManipulator<Identifier>() {
    override fun handleContentChange(element: Identifier, range: TextRange, newContent: String): Identifier = element.setName(newContent) as Identifier
}
