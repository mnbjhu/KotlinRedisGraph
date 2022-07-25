import attributes.DoubleAttribute
import attributes.IntAttribute
import attributes.RedisAttribute
import attributes.StringAttribute
import scopes.CreatePathScope

open class RedisClass(
    val name: String
){
    private val attributes: MutableList<in RedisAttribute> = mutableListOf()
    private val relations: MutableList<in RedisRelation<RedisClass>> = mutableListOf()



    fun string(name: String) = StringAttribute(name)
    fun double(name: String) = DoubleAttribute(name)
    fun int(name: String) = IntAttribute(name)

}