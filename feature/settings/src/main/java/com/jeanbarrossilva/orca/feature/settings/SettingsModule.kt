package com.jeanbarrossilva.orca.feature.settings

import com.jeanbarrossilva.orca.core.feed.profile.toot.muting.TermMuter
import com.jeanbarrossilva.orca.std.injector.module.Inject
import com.jeanbarrossilva.orca.std.injector.module.Module

abstract class SettingsModule(
  @Inject internal val termMuter: Module.() -> TermMuter,
  @Inject internal val boundary: Module.() -> SettingsBoundary
) : Module()
