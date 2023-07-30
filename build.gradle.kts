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

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
