import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

gradlePlugin {
    plugins.register(name) {
        id = name
        implementationClass = "com.jeanbarrossilva.orca.plugin.BuildSrcPlugin"
    }
}

tasks.withType<KotlinCompile> {
    with(rootDir.parentFile.properties("gradle").getProperty("project.java")) {
        java {
            sourceCompatibility = JavaVersion.toVersion(this@with)
            targetCompatibility = JavaVersion.toVersion(this@with)
        }

        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(this))
    }
}

/**
 * Creates [Properties] according to the `.properties` file named [name] within this directory.
 *
 * @param name Name of the file.
 **/
fun File.properties(name: String): Properties {
    val file = File(this, "$name.properties")
    return Properties().apply { tryToLoad(file) }
}

/**
 * Loads the given [file] into these [Properties].
 *
 * @param file [File] to be loaded.
 **/
fun Properties.load(file: File) {
    file.inputStream().reader().use {
        load(it)
    }
}

/**
 * Loads the given [file] into these [Properties] if it's a normal file.
 *
 * @param file [File] to be loaded.
 **/
fun Properties.tryToLoad(file: File) {
    if (file.isFile) {
        load(file)
    }
}
