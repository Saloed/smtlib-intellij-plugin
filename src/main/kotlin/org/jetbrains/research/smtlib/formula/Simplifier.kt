package org.jetbrains.research.smtlib.formula

import com.intellij.openapi.diagnostic.Logger
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import org.jetbrains.research.smtlib.utils.ClassLoaderUtils

val LOG: Logger = Logger.getInstance(Simplifier::class.java)

class Simplifier {
    fun simplifyAssertStatement(formula: String, assertIdx: Int): String = Context().use {
        val asserts = it.parseSMTLIB2String(formula, null, null, null, null)
        val expression = asserts[assertIdx]
        val simplified = it.simplifyExpression(expression)
        "(assert $simplified)"
    }

    private fun Context.simplifyExpression(expr: Expr): Expr = expr.simplify()

    companion object {
        init {
            when (val z3Home = System.getenv("Z3_HOME")) {
                null -> LOG.warn("No Z3_HOME variable in system environment")
                else -> ClassLoaderUtils.ensureLibraryPath(z3Home)
            }
        }
    }
}