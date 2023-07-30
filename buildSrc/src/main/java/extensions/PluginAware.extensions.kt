import org.gradle.api.plugins.PluginAware

/** Whether this is an Android library. **/
val PluginAware.isAndroidLibrary
    get() = plugins.hasPlugin("com.android.library")
