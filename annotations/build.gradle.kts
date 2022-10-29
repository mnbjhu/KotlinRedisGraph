plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
}
dependencies {
    api("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")
    api("com.squareup:kotlinpoet:1.12.0")
    api("com.squareup:kotlinpoet-metadata:1.12.0")
    implementation(project(":core"))
    api("com.natpryce:konfig:1.6.10.0")
    testApi("junit:junit:4.13.2")
    testApi("org.amshove.kluent:kluent:1.72")
    kaptTest(project(":annotations"))
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