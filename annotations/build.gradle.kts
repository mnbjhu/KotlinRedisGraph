dependencies {
    implementation("com.google.auto.service:auto-service:1.0.1")
    kapt("com.google.auto.service:auto-service:1.0.1")
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.squareup:kotlinpoet-metadata:1.12.0")
    implementation(project(":core"))
    implementation(kotlin("test"))
    implementation("com.natpryce:konfig:1.6.10.0")
    testImplementation(kotlin("test"))
    testImplementation("org.amshove.kluent:kluent:1.68")
    kaptTest(project(":annotations"))
}