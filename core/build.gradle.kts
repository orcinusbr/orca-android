plugins {
    // noinspection JavaPluginLanguageLevel
    id("java-library")

    kotlin("jvm")
}

dependencies {
    api(project(":std:styled-string"))
    api(libs.kotlin.coroutines.core)

    testImplementation(project(":core:sample"))
    testImplementation(project(":core-test"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.coroutines.test)
}
