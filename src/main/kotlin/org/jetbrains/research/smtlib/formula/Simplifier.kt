package org.jetbrains.research.smtlib.formula

import com.intellij.openapi.diagnostic.Logger
import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.Params
import com.microsoft.z3.StringSymbol
import com.microsoft.z3.enumerations.Z3_param_kind

val LOG: Logger = Logger.getInstance(Simplifier::class.java)

@OptIn(ExperimentalUnsignedTypes::class)
object Simplifier {
    fun simplifyAssertStatement(formula: String, params: List<SimplifierParam<*>>?) = Context().use { ctx ->
        ctx.parseSMTLIB2String(formula, null, null, null, null)
            .map { ctx.simplifyExpression(it, params) }
            .map { "(assert $it)" }
    }

    private fun Context.simplifyExpression(expr: Expr<*>, params: List<SimplifierParam<*>>?): Expr<*> {
        val simplifyParams = params?.let { p ->
            mkParams().apply {
                p.forEach { add(it) }
            }
        }
        LOG.info("Simplify params: $simplifyParams")
        return expr.simplify(simplifyParams)
    }


    val simplificationParameters: List<SimplifierParam<*>> by lazy {
        mkSimplificationParams()
    }

    private fun mkSimplificationParams() = Context().use { ctx ->
        val descriptors = ctx.simplifyParameterDescriptions
        val params = descriptors.names.filterIsInstance<StringSymbol>()
        params.mapNotNull { paramName ->
            val description = descriptors.getDocumentation(paramName)
            when (descriptors.getKind(paramName)) {
                Z3_param_kind.Z3_PK_UINT -> IntSimplifierParam(
                    name = paramName.string,
                    description = description,
                    default = intParameterDefault(paramName.string)
                )
                Z3_param_kind.Z3_PK_BOOL -> BoolSimplifierParam(
                    name = paramName.string,
                    description = description,
                    default = boolParameterDefault(paramName.string)
                )
                else -> null
            }
        }
    }

    private fun Params.add(param: SimplifierParam<*>) {
        if (param.value == param.default) return
        when (param) {
            is BoolSimplifierParam -> add(param.name, param.value)
            is IntSimplifierParam -> add(param.name, param.value.toInt())
        }
    }

    private fun boolParameterDefault(name: String): Boolean = when (name) {
        in trueBoolParameters -> true
        else -> false
    }

    private fun intParameterDefault(name: String): UInt = specialIntParameters[name] ?: UInt.MAX_VALUE

    private val trueBoolParameters = setOf(
        "algebraic_number_evaluator",
        "bit2bool",
        "elim_ite",
        "elim_sign_ext",
        "flat",
        "hi_div0",
        "ignore_patterns_on_ground_qbody",
        "push_to_real",
    )

    private val specialIntParameters = mapOf(
        "bv_ineq_consistency_test_max" to 0U,
        "div0_ackermann_limit" to 1000U,
        "max_degree" to 64U,
        "som_blowup" to 10U,
    )

    sealed class SimplifierParam<T> {
        abstract val name: String
        abstract val description: String
        abstract val value: T
        abstract val default: T
        abstract fun update(value: T): SimplifierParam<T>
    }

    data class BoolSimplifierParam(
        override val name: String,
        override val description: String,
        override val default: Boolean,
        override val value: Boolean = default
    ) : SimplifierParam<Boolean>() {
        override fun update(value: Boolean) = BoolSimplifierParam(name, description, default, value)
    }

    data class IntSimplifierParam(
        override val name: String,
        override val description: String,
        override val default: UInt,
        override val value: UInt = default
    ) : SimplifierParam<UInt>() {
        override fun update(value: UInt) = IntSimplifierParam(name, description, default, value)
    }

//
//    init {
//        when (val z3Home = System.getenv("Z3_HOME")) {
//            null -> LOG.warn("No Z3_HOME variable in system environment")
//            else -> ClassLoaderUtils.ensureLibraryPath(z3Home)
//        }
//    }
}
