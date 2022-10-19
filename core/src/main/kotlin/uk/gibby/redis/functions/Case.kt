package uk.gibby.redis.functions

import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

inline fun <T, U: ResultValue<T>, V, reified R: ResultValue<V>>switch(target: U, type: KClass<R>? = null, scope: CaseBuilder<T, V>.() -> CaseBranch<T, V>): ResultValue<V>{
    return CaseBuilder(target, R::class).apply { scope() }.build()
}


/*
class TargetedCaseHead<T, U: ResultValue<T>>(private val target: U){
    operator fun <V, R: ResultValue<V>>invoke(scope: CaseBuilder<T, U, V, R>.() -> CaseBranch<T, U, V, R>){
        CaseBuilder<T, U, V, R>(this, target).apply { scope() }.build()
    }
}*/
class CaseBuilder<T, V>(private val target: ResultValue<T>, private val clazz: KClass<out ResultValue<V>>){
    private val cases = mutableListOf<CaseBranch<*, *>>()
    infix fun <R: ResultValue<V>>T.then(result: R) = CaseBranch.LiteralToReference(this, result, target).also { cases.add(it) }
    infix fun <U: ResultValue<T>, R: ResultValue<V>>U.then(result: R) = CaseBranch.ReferenceToReference(this, result).also { cases.add(it) }
    infix fun T.then(result: V) = CaseBranch.LiteralToLiteral(this, result, target,  clazz).also { cases.add(it) }
    infix fun <U: ResultValue<T>>U.then(result: V) = CaseBranch.ReferenceToLiteral(this, result, clazz).also { cases.add(it) }
    fun build(): ResultValue<V> {
        val switch = TargetedCase(target, cases.toList() as List<CaseBranch<T, V>>)
        val instance = clazz.primaryConstructor?.call() ?: throw Exception("Class should have a primary constructor with 1 argument. (${clazz.simpleName})")
        instance.reference = switch.getString()
        return instance
    }
}
class TargetedCase<T, V>(private val target: ResultValue<T>, private val cases: List<CaseBranch<T, V>>) {
    fun getString(): String {
        val branches = cases.joinToString(separator = " ") { it.getString() }
        return "CASE ${target.getString()} $branches END"
    }
}

abstract class CaseBranch<T, V>{
    abstract fun getString(): String

    class LiteralToReference<T, V>(private val from: T, private val to: ResultValue<in V>, private val target: ResultValue<T>): CaseBranch<T, V>() {
        override fun getString(): String =
            "WHEN ${target.getLiteral(from)} THEN ${to.getString()}"
    }
    class ReferenceToReference<T, V>(private val from: ResultValue<T>, private val to: ResultValue<in V>): CaseBranch<T, V>() {
        override fun getString(): String =
            "WHEN ${from.getString()} THEN ${to.getString()}"
    }
    class LiteralToLiteral<T, V>(private val from: T, private val to: V, private val target: ResultValue<T> ,private val clazz: KClass<out ResultValue<in V>>): CaseBranch<T, V>() {
        override fun getString(): String =
            "WHEN ${target.getLiteral(from)} THEN ${clazz.primaryConstructor!!.call().getLiteral(to)}"
    }
    class ReferenceToLiteral<T, V>(private val from: ResultValue<T>, private val to: V, private val clazz: KClass<out ResultValue<in V>>): CaseBranch<T, V>() {
        override fun getString(): String =
            "WHEN ${from.getString()} THEN ${clazz.primaryConstructor!!.call().getLiteral(to)}"
    }
}



