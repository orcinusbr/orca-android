plugins {
    alias(libs.plugins.kotlin.jvm)

    `java-library`
}

dependencies.implementation(project(":std:image-loader"))
