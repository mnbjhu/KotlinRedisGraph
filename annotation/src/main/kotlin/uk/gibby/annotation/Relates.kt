package uk.gibby.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Relates(
    val to: KClass<*>,
    val by: String,
    val data: KClass<*> = EmptyRelation::class
)