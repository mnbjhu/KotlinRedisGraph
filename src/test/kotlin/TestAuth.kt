import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Key
import com.natpryce.konfig.intType
import com.natpryce.konfig.stringType

object TestAuth {
    val config = ConfigurationProperties.fromResource("local.properties")
    val server_port = Key("server.port", intType)
    val server_host = Key("server.host", stringType)
    val server_pass = Key("server.pass", stringType)
    val host = config[server_host]
    val port = config[server_port]
    val password = config[server_pass]

}
