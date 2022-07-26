package uk.gibby.redis.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@Repeatable
annotation class Relates(
    val to: KClass<*>,
    val by: String,
    val data: KClass<*>
)