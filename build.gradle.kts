buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Plugins.GRADLE)
        classpath(Plugins.KOTLIN)
        classpath(Plugins.NAVIGATION_SAFE_ARGS)
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
