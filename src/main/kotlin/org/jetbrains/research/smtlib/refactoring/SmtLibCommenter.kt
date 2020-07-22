package org.jetbrains.research.smtlib.refactoring

import com.intellij.lang.Commenter

class SmtLibCommenter : Commenter{
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
    override fun getBlockCommentPrefix(): String?  = null
    override fun getBlockCommentSuffix(): String?  = null
    override fun getLineCommentPrefix(): String? = ";"

}