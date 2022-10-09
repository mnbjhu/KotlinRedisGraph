dependencies{
    implementation(project(":core"))
    implementation(project(":annotation"))
    kapt(project(":kapt-tool"))
    implementation(kotlin("test"))
}