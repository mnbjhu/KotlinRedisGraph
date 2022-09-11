import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.conditions.equality.SerializableEquality.Companion.eq
import org.junit.jupiter.api.Test
import uk.gibby.redis.statements.Match.Companion.match
import schemas.EnumSchema
import schemas.MyEnum
import uk.gibby.redis.statements.Where.Companion.where

class SerializableTest {
    private val enumGraph = RedisGraph("EnumGraph",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun test(){
        enumGraph.create(EnumSchema::class){
           it[enum] = MyEnum.B
        }
        val x =  enumGraph.query {
            val enum = match(EnumSchema())
            where(enum.enum eq MyEnum.B)
            enum.enum
        }
        println(x)
    }
}