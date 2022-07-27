package api

import attributes.HasAttributes

inline fun <reified T: RedisClass>create(createScope: T.(MutableMap<HasAttributes.Attribute<Any>, Any>) -> Unit): String{
    val instance = T::class.constructors.first().call("")
    with(instance){
        createScope(values)
    }
    return "CREATE (:${ instance.typeName }{${
        instance.values.map {
            with(it) { "${key.name} = ${if (value is String) "'${value}'" else value}" }
        }.joinToString()
    }})"
}