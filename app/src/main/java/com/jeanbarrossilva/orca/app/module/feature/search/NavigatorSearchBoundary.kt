package com.jeanbarrossilva.orca.app.module.feature.search

import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.search.SearchBoundary
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class NavigatorSearchBoundary(private val navigator: Navigator) : SearchBoundary {
  override fun navigateToProfileDetails(id: String) {
    ProfileDetailsFragment.navigate(navigator, id)
  }

  override fun pop() {
    navigator.pop()
  }
}
