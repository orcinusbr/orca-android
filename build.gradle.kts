import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Plugins.GRADLE)
        classpath(Plugins.KOTLIN)
        classpath(Plugins.SECRETS)
    }
}

allprojects {
    repositories {
        chrynan()
        google()
        loadable(project)
        mavenCentral()
    }
}

tasks {
    allprojects {
        tasks.withType<KotlinCompile>().configureEach {
            compilerOptions.jvmTarget.set(JvmTarget.fromTarget("${Versions.java}"))
        }
    }

    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
}
