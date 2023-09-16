plugins {
    // noinspection JavaPluginLanguageLevel
    id("java-library")

    id("org.jetbrains.kotlin.jvm")
}

dependencies {
    api(project(":core"))

    testImplementation(project(":core:sample-test"))
    testImplementation(kotlin("test"))
    testImplementation(libs.kotlin.coroutines.test)
}
