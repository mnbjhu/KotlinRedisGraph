package attributes

import api.WithAttributes


/**
 * Attribute
 *
 * @param T
 */
sealed interface Attribute<T> {
    val parent: WithAttributes
    val name: String

    /**
     * Get attribute text
     *
     */
    fun getAttributeText() = "${parent.instanceName}.$name"
}