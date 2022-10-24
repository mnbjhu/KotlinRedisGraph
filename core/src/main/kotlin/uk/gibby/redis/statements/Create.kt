package uk.gibby.redis.statements

import uk.gibby.redis.core.*
import uk.gibby.redis.paths.Path
import uk.gibby.redis.scopes.EmptyResult
import uk.gibby.redis.scopes.QueryScope

class Create(private val toCreate: List<Creatable>) : Statement() {
    override fun getCommand(): String = "CREATE ${toCreate.joinToString { it.getCreateString() }}"

    companion object {
        fun QueryScope.create(path: Path) = EmptyResult
            .also { commands.add(Create(listOf(path))) }
        fun <a, A : RedisNode<a>>QueryScope.create(node: () -> A): A = node().let {
            commands.add(Create(listOf(it)))
            (it.copyType() as A).apply {
                matched = true
                NameSetter.set(with(it) { NameSetter.current })
            }
        }
        @JvmName("createNode")
        fun <A : RedisNode<*>, B : RedisNode<*>> QueryScope.create(node1: () -> A, node2: () -> B): Pair<A, B> {
            val (first, second) = node1() to node2()
            commands.add(Create(listOf(first, second)))
            first.matched = true; second.matched = true
            return first to second
        }

    }
}