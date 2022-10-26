plugins {
    id("org.jetbrains.dokka") version "1.7.10"
    `java-library`
    `maven-publish`
}
dependencies {
    implementation("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-metadata:1.12.0")
    implementation(project(":core"))
    implementation(kotlin("test"))
    implementation("com.natpryce:konfig:1.6.10.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.amshove.kluent:kluent:1.68")
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