package api

import attributes.RelationAttribute
import kotlin.reflect.KClass

operator fun <T: RedisNode, U: RedisNode, V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>>T.minus(scope: T.() -> W) =
    OpenPath1(this, scope().relation)
class OpenPath1<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode,>
    (val first: A, val firstToSecond: KClass<B>){
        operator fun minus(node: C) = Path2(first, firstToSecond.constructors.first().call(first, node), node)
    }
class Path2<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode>
    (val first: A, val firstToSecond: B, val second: C): Matchable {
    operator fun <E : RedisNode, D : RedisRelation<C, E>, W : RelationAttribute<C, E, D>> minus(scope: C.() -> W) =
        OpenPath2(first, firstToSecond, second, second.scope().relation)

    override fun toString(): String = "$first-$firstToSecond->$second"


}
class OpenPath2<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, val firstToSecond: B, val second: C, val secondToThird: KClass<D>)
class Path3<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, val firstToSecond: B, val second: C, val secondToThird: D, val third: E)

class Path4<
        A : RedisNode, B : RedisRelation<A, C>,
        C : RedisNode, D : RedisRelation<C, E>,
        E : RedisNode, F : RedisRelation<E, G>,
        G : RedisNode>
    (
    val first: A, val firstToSecond: B,
    val second: C, val secondToThird: D,
    val third: E, val thirdToForth: F,
    val forth: G
)
class Query(
    statements: List<RedisStatement>
)
sealed class RedisStatement
class Match(val args: List<Matchable>): RedisStatement(){
    override fun toString() = "MATCH ${args.joinToString()}"
}

interface Matchable {
    override fun toString(): String
}

/*
operator fun <A: RedisNode, B: RedisRelation<A, C>, C: RedisNode, D: RedisRelation<C, E>, E: RedisNode>
        CreatePathScope.RedisNodeRelationPair<A, C, B>.minus(other: CreatePathScope.RedisNodeRelationPair<C, E, D>):
        OpenPath2<A, B, C, D, E> = OpenPath2(this.node, this.relation.)
*/