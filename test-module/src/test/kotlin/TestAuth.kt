import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType
import java.io.File

object TestAuth {
    private val config = ConfigurationProperties.fromResource("local.properties")
    private val server_port = Key("server.port", intType)
    private val server_host = Key("server.host", stringType)
    private val server_pass = Key("server.pass", stringType)
    val host = config[server_host]
    val port = config[server_port]
    val password = config[server_pass]
}
