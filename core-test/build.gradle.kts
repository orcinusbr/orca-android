plugins {
    alias(libs.plugins.kotlin.jvm)

    `java-library`
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core:sample"))
}
