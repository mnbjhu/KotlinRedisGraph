package api

import attributes.HasAttributes

inline fun <reified T: RedisClass>create(createScope: T.() -> Unit): String{
    val instance = T::class.constructors.first().call("")
    instance.createScope()
    return "CREATE (:${ instance.typeName }{${
        instance.attributes.map {
            "${it.name} = ${if (it.value is String) "'${it.value}'" else it.value}"
        }.joinToString()
    }})"
}
inline fun <reified T: RedisClass, U>create(items: List<U>, createScope: T.(U) -> Unit): String{
    val instance = T::class.constructors.first().call("")
    val itemsString = items.map { item ->
        instance.createScope(item)
        instance.attributes.map {
            "(:${instance.typeName}{${
                instance.attributes.joinToString { attr ->
                    "${it.name} = ${if (it.value is String) "'${it.value}'" else it.value}"
                }
            }})"
        }
    }.joinToString()
    return "CREATE $itemsString"
}
