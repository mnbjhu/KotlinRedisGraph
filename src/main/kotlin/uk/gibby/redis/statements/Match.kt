package uk.gibby.redis.statements

import uk.gibby.redis.core.Matchable
import uk.gibby.redis.core.RedisNode
import uk.gibby.redis.paths.Path
import uk.gibby.redis.scopes.QueryScope

class Match(private val toMatch: List<Matchable>) : Statement() {
    override fun getCommand(): String = "MATCH ${toMatch.joinToString { it.getMatchString() }}"

    companion object {
        fun <A : RedisNode> QueryScope.match(node: A) = node.also { commands.add(Match(listOf(it))) }
        fun <A : RedisNode, B : RedisNode> QueryScope.match(node1: A, node2: B): Pair<A, B> {
            commands.add(Match(listOf(node1, node2)))
            return node1 to node2
        }

        fun <T : Path> QueryScope.match(path: T) = path.also { commands.add(Match(listOf(it))) }
    }
}