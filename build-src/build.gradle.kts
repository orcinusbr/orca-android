import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

with(JavaVersion.VERSION_17) javaVersion@{
    System.setProperty("JAVA_VERSION", "$this")

    java {
        sourceCompatibility = this@javaVersion
        targetCompatibility = this@javaVersion
    }

    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget("${this@javaVersion}"))
    }
}

gradlePlugin {
    plugins.register("build-src") {
        id = "build-src"
        implementationClass = "com.jeanbarrossilva.orca.plugin.BuildSrcPlugin"
    }
}
