import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
}

group = "uk.gibby"
version = "0.2.0"


repositories {
    mavenCentral()
}

dependencies {
    implementation("redis.clients:jedis:4.2.3")
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    testImplementation("org.amshove.kluent:kluent:1.68")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])

                groupId = "com.github.mnbjhu"
                artifactId = "redis-kotlin"
                version = "0.0.1"
            }
        }
    }
}
