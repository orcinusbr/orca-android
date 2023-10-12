package com.jeanbarrossilva.orca.app.module.feature.settings.termmuting

import com.jeanbarrossilva.orca.feature.settings.termmuting.TermMutingBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class NavigatorTermMutingBoundary(private val navigator: Navigator) : TermMutingBoundary {
  override fun pop() {
    navigator.pop()
  }
}
