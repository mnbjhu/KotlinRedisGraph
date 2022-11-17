import uk.gibby.redis.annotation.Node
import uk.gibby.redis.annotation.UsingType
import uk.gibby.redis.core.ParamMap
import uk.gibby.redis.core.RedisGraph
import uk.gibby.redis.results.LongResult
import uk.gibby.redis.results.ResultScope
import uk.gibby.redis.results.StructResult
import java.util.Date


class DateResult: StructResult<Date>() {
    var inSeconds by LongResult()
    override fun ResultScope.getResult(): Date {
        return Date(inSeconds.result())
    }

    override fun ParamMap.setResult(value: Date) {
        inSeconds[value.time]
    }
}

@Node
data class Message(val message: String, @UsingType(DateResult::class) val sentAt: Date)

class UsingTests {
    private val graph = RedisGraph(
        name = "UsersTest",
        host = TestAuth.host,
        port = TestAuth.port,
        password = TestAuth.password
    )

}