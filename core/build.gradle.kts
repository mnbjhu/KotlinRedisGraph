
plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
}

dependencies {
    implementation("redis.clients:jedis:4.2.3")
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    testImplementation("org.amshove.kluent:kluent:1.68")
    testImplementation("junit:junit:4.13.2")
    //testImplementation(kotlin("test"))

}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}

