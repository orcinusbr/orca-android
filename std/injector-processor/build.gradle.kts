plugins {
    alias(libs.plugins.kotlin.jvm)

    `java-library`
}

dependencies {
    implementation(project(":std:injector"))
    implementation(libs.kotlin.symbolProcessor)
    implementation(libs.kotlinPoet)
    implementation(libs.kotlinPoet.ksp)
}
