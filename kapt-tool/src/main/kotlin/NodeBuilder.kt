import uk.gibby.annotation.Relates
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

fun <T: Any>generateNodeClass(
    members: String,
    relations: Relates
){
    type.memberProperties
        .map {
            it.name to when(it.typeParameters.first()){
                String::class -> "StringAttribute()"
                Long::class -> "LongAttribute()"
                else -> throw Exception()
            }
        }
}