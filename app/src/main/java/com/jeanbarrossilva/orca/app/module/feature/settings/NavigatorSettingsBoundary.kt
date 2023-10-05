package com.jeanbarrossilva.orca.app.module.feature.settings

import com.jeanbarrossilva.orca.feature.settings.SettingsBoundary
import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class NavigatorSettingsBoundary(private val navigator: Navigator) : SettingsBoundary {
    override fun navigateToTermMuting() {
        TermMutingFragment.navigate(navigator)
    }
}
