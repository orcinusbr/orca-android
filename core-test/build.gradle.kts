plugins {
    // noinspection JavaPluginLanguageLevel
    id("java-library")

    id("org.jetbrains.kotlin.jvm")
}

dependencies.implementation(project(":core"))
