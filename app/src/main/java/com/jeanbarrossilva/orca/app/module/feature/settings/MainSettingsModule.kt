package com.jeanbarrossilva.orca.app.module.feature.settings

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.feature.settings.SettingsBoundary
import com.jeanbarrossilva.orca.feature.settings.SettingsModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injector

internal class MainSettingsModule(private val navigator: Navigator) : SettingsModule() {
    override fun Injector.termMuter(): TermMuter {
        return get()
    }

    override fun Injector.boundary(): SettingsBoundary {
        return NavigatorSettingsBoundary(navigator)
    }
}
