package api

import attributes.RelationAttribute
import conditions.True
import kotlin.reflect.KClass

operator fun <T: RedisNode, U: RedisNode, V: RedisRelation<T, U>, W: RelationAttribute<T, U, V>>T.minus(scope: T.() -> W) =
    OpenPath1(this, scope().relation)
class OpenPath1<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode,>
    (val first: A, val firstToSecond: KClass<B>){
        operator fun minus(node: C) = Path2(first, firstToSecond.constructors.first().call(first, node), node)
    }
class Path2<A: RedisNode, B: RedisRelation<A, C>, C: RedisNode>
    (val first: A, val firstToSecond: B, val second: C): Path {
    operator fun <E : RedisNode, D : RedisRelation<C, E>, W : RelationAttribute<C, E, D>> minus(scope: C.() -> W) =
        OpenPath2(first, firstToSecond, second, second.scope().relation)
    operator fun <X: RedisNode, Y: RedisRelation<X, Z>, Z: RedisNode>C.invoke(scope: Y.() -> Unit): C{
        TODO()
    }
    override fun getMatchString(): String = "$first-$firstToSecond->$second"
    override fun getCreateString(): String = "(${first.instanceName})-$firstToSecond->(${first.instanceName})"

}

class Path3<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, val firstToSecond: B, val second: C, val secondToThird: D, val third: E): Matchable {
    override fun getMatchString() = "$first-$firstToSecond->$second-$secondToThird->$third"
}
class OpenPath2<A : RedisNode, B : RedisRelation<A, C>, C : RedisNode, D : RedisRelation<C, E>, E : RedisNode>
    (val first: A, val firstToSecond: B, val second: C, val secondToThird: KClass<D>)
class Path4<
        A : RedisNode, B : RedisRelation<A, C>,
        C : RedisNode, D : RedisRelation<C, E>,
        E : RedisNode, F : RedisRelation<E, G>,
        G : RedisNode
        >
    (
    val first: A, val firstToSecond: B,
    val second: C, val secondToThird: D,
    val third: E, val thirdToForth: F,
    val forth: G
)
class Query<T>(
    var match: MatchStatement,
    val where: ResultValue.BooleanResult = True,
    var resultValues: List<ResultValue<*>>,
    var transform: (() -> T)? = null,
    var orderBy: ResultValue<*>? = null,
    var create: CreateStatement? = null,
    var toDelete: List<WithAttributes>,
)

class MatchStatement(private val args: List<Matchable>){
    override fun toString() = "MATCH ${args.joinToString{ it.getMatchString() }}"
}
class CreateStatement(private val args: List<Creatable>){
    override fun toString() = "MATCH ${args.joinToString()}"
}

interface Matchable {
    fun getMatchString(): String
}
interface Path: Matchable, Creatable

interface Creatable{
    fun getCreateString(): String
}



/*
operator fun <A: RedisNode, B: RedisRelation<A, C>, C: RedisNode, D: RedisRelation<C, E>, E: RedisNode>
        CreatePathScope.RedisNodeRelationPair<A, C, B>.minus(other: CreatePathScope.RedisNodeRelationPair<C, E, D>):
        OpenPath2<A, B, C, D, E> = OpenPath2(this.node, this.relation.)
*/