package uk.gibby.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class Relates(val to: KClass<*>, val by: KClass<*>)

@Target(AnnotationTarget.CLASS)
annotation class RelatesTo(val to: KClass<*>, val name: String)