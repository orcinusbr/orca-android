package com.jeanbarrossilva.orca.app.module.feature.settings

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.feature.settings.SettingsModule
import com.jeanbarrossilva.orca.std.injector.Injectable

internal object MainSettingsModule : SettingsModule() {
    override val termMuter = Injectable<TermMuter> {
        get()
    }
}
