dependencies{
    implementation(project(":core"))
    implementation(project(":annotation"))
    kapt(project(":kapt-tool"))
    kaptTest(project(":kapt-tool"))
    implementation(kotlin("test"))
    implementation("com.natpryce:konfig:1.6.10.0")
    testImplementation(kotlin("test"))
    testImplementation("org.amshove.kluent:kluent:1.68")

}