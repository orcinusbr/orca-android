plugins {
    // noinspection JavaPluginLanguageLevel
    id("java-library")

    kotlin("jvm")
}

dependencies.implementation(project(":std:image-loader"))
