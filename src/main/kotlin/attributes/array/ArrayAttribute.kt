package attributes.array

import attributes.Attribute
import core.WithAttributes

class ArrayAttribute<T, U: Attribute<T>>(val item: U, override val parent: WithAttributes, override val name: String): Attribute<List<T>>() {

}