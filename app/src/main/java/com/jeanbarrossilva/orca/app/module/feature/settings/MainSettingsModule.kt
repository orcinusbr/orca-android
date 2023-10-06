package com.jeanbarrossilva.orca.app.module.feature.settings

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.feature.settings.SettingsBoundary
import com.jeanbarrossilva.orca.feature.settings.SettingsModule
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.std.injector.Injectable

internal class MainSettingsModule(navigator: Navigator) : SettingsModule() {
    override val termMuter = Injectable<TermMuter> { get() }
    override val boundary = Injectable<SettingsBoundary> { NavigatorSettingsBoundary(navigator) }
}
