package uk.gibby.redis.core

import uk.gibby.redis.paths.NameCounter
import uk.gibby.redis.results.Attribute

sealed class WithAttributes: AttributeParent {
    var params: List<ParameterPair<*>>? = null
    abstract val typeName: String
    abstract override val attributes: MutableSet<Attribute<*>>
    override var instanceName = NameCounter.getNext()
    protected inline fun <reified T : Any>serializable() = serializable(T::class)

}
operator fun <T : WithAttributes> T.invoke(scope: T.(ParamMap) -> Unit) {
    val params = ParamMap()
    scope(params)
    this.params = params.getParams()
}
