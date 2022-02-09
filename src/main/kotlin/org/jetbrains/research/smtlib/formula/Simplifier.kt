package org.jetbrains.research.smtlib.formula

import com.intellij.openapi.diagnostic.Logger
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.StringSymbol

val LOG: Logger = Logger.getInstance(Simplifier::class.java)

object Simplifier {
    fun simplifyAssertStatement(formula: String, params: Map<String, Boolean>) = Context().use { ctx ->
        ctx.parseSMTLIB2String(formula, null, null, null, null)
            .map { ctx.simplifyExpression(it, params) }
            .map { "(assert $it)" }
    }

    private fun Context.simplifyExpression(expr: Expr<*>, params: Map<String, Boolean>): Expr<*> {
        val simplifyParams = mkParams()
        for ((param, value) in params) {
            simplifyParams.add(param, value)
        }
        LOG.info("Simplify params: $simplifyParams")
        return expr.simplify(simplifyParams)
    }

    fun simplificationParameters(): Map<String, Pair<String, Boolean>> = Context().use { ctx ->
        val descriptors = ctx.simplifyParameterDescriptions
        val params = descriptors.names.filterIsInstance<StringSymbol>()
        val descriptions = params.map { descriptors.getDocumentation(it) }
        val names = params.map { it.string }
        names.zip(descriptions).map { (param, desc) ->
            param to (desc to parametersDefaults.getOrDefault(param, false))
        }.toMap()
    }

    private val parametersDefaults = mapOf(
        "algebraic_number_evaluator" to true,
        "bit2bool" to true,
        "elim_ite" to true,
        "elim_sign_ext" to true,
        "flat" to true,
        "hi_div0" to true,
        "ignore_patterns_on_ground_qbody" to true,
        "push_to_real" to true
    )
//
//    init {
//        when (val z3Home = System.getenv("Z3_HOME")) {
//            null -> LOG.warn("No Z3_HOME variable in system environment")
//            else -> ClassLoaderUtils.ensureLibraryPath(z3Home)
//        }
//    }
}
