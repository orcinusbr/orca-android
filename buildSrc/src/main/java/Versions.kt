import org.gradle.api.JavaVersion

object Versions {
    const val ACCOMPANIST = "0.31.3-beta"
    const val COIL = "2.4.0"
    const val COMPOSE_COMPILER = "1.4.7"
    const val COMPOSE_DESTINATIONS = "1.9.42-beta"
    const val COMPOSE_MATERIAL_3 = "1.2.0-alpha02"
    const val COMPOSE_MATERIAL_ICONS_EXTENDED = "1.4.3"
    const val COMPOSE_UI_TOOLING = "1.4.3"
    const val COROUTINES = "1.7.1"
    const val LOADABLE = "1.5.2"
    const val GRADLE = "8.0.2"
    const val KOIN = "3.4.4"
    const val KOTLIN = "1.8.21"
    const val KSP = "1.8.21-1.0.11"
    const val MATERIAL = "1.9.0"
    const val VIEWMODEL = "2.6.1"

    val java = JavaVersion.VERSION_17

    object Mastodonte {
        const val CODE = 1
        const val NAME = "1.0.0"
        const val SDK_COMPILE = 33
        const val SDK_MIN = 24
        const val SDK_TARGET = SDK_COMPILE
    }
}
