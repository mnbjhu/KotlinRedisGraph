package api


/**
 * Attribute
 *
 * @param T
 */
interface Attribute<T> {
    val parent: WithAttributes
    val name: String

    /**
     * Get attribute text
     *
     */
    fun getAttributeText() = "${parent.instanceName}.$name"
}