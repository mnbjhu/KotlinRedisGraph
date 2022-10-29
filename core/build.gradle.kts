
plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
}

dependencies {
    api("redis.clients:jedis:4.3.1")
    api("com.natpryce:konfig:1.6.10.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation(project(":annotations"))
    testApi("org.amshove.kluent:kluent:1.72")
    testApi("junit:junit:4.13.2")
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

