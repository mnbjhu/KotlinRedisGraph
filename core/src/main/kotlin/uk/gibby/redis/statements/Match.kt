package uk.gibby.redis.statements

import uk.gibby.redis.core.Matchable
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.paths.Path
import uk.gibby.redis.scopes.QueryScope
import kotlin.reflect.KFunction
import kotlin.reflect.KFunction0

class Match(private val toMatch: List<Matchable>) : Statement() {
    override fun getCommand(): String = "MATCH ${toMatch.joinToString { it.getMatchString() }}"

    companion object {
        @Deprecated(message = "Matching and instance is deprecated as of 0.8.0")
        fun <A : RedisNode<*>> QueryScope.match(node: A) = node.also {
            commands.add(Match(listOf(it)))
            it.matched = true
        }

        fun <A : RedisNode<*>> QueryScope.match(node: () -> A) = node().also {
            commands.add(Match(listOf(it)))
            it.matched = true
        }
        @Deprecated(message = "Matching and instance is deprecated as of 0.8.0")
        fun <A : RedisNode<*>, B : RedisNode<*>> QueryScope.match(node1: A, node2: B): Pair<A, B> {
            commands.add(Match(listOf(node1, node2)))
            node1.matched = true; node2.matched = true
            return node1 to node2
        }
        fun <A : RedisNode<*>, B : RedisNode<*>> QueryScope.match(node1: () -> A, node2: () -> B): Pair<A, B> {
            val (first, second) = node1() to node2()
            commands.add(Match(listOf(first, second)))
            first.matched = true; second.matched = true
            return first to second
        }

        fun <T : Path> QueryScope.match(path: T) = path.also {
                commands.add(Match(listOf(it)))
        }
    }
}