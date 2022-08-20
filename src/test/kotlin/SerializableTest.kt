import api.RedisGraph
import conditions.equality.SerializableEquality.Companion.eq
import org.junit.jupiter.api.Test

import schemas.EnumSchema
import schemas.MyEnum

class SerializableTest {
    val enumGraph = RedisGraph("EnumGraph",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )
    @Test
    fun test(){
        enumGraph.create(EnumSchema::class){
            enum[MyEnum.B]
        }
        val x =  enumGraph.query {
            val enum = match(EnumSchema())
            where(enum.enum eq MyEnum.B)
            result(enum.enum)
        }
        println(x)
    }
}