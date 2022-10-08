

dependencies {
    implementation("redis.clients:jedis:4.2.3")
    implementation(kotlin("reflect"))
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    testImplementation("org.amshove.kluent:kluent:1.68")
    testImplementation(kotlin("test"))
    implementation(project(":annotation"))
    kapt(project(":kapt-tool"))
}

