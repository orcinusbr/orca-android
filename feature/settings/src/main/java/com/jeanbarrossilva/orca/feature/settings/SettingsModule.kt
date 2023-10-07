package com.jeanbarrossilva.orca.feature.settings

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.module.Dependency
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class SettingsModule(
    @Dependency private val termMuter: Module.() -> TermMuter,
    @Dependency private val boundary: Module.() -> SettingsBoundary
) : Module()
