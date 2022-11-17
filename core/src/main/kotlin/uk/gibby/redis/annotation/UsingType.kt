package uk.gibby.redis.annotation

import uk.gibby.redis.results.ResultValue
import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class UsingType(val clazz: KClass<out ResultValue<*>>)
