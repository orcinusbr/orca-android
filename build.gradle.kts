buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Plugins.GRADLE)
        classpath(Plugins.KOTLIN)
    }
}

allprojects {
    repositories {
        google()
        loadable(project)
        mavenCentral()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

afterEvaluate {
    allprojects {
        if (isAndroidLibrary) {
            tasks.named("testReleaseUnitTest") {
                enabled = false
            }
        }
    }
}
