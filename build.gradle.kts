
//import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
/*
plugins {
    kotlin("jvm") version "1.7.0"
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
}*/

group = "uk.gibby"
version = "0.3.3"


buildscript {
    repositories {
        mavenCentral()
    }
    
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

subprojects {

    apply(plugin = "kotlin")
    apply(plugin = "kotlin-kapt")

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"(kotlin("stdlib", version = "1.7.10"))
        //"implementation"(kotlin("test", version = "1.7.10"))
        "implementation"(kotlin("reflect", version = "1.7.10"))
    }
}/*
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
                version = "0.3.3"
            }
        }
    }
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}*/