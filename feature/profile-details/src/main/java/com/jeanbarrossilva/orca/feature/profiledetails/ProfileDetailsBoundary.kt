package com.jeanbarrossilva.orca.feature.profiledetails

import java.net.URL

interface ProfileDetailsBoundary {
  fun navigateTo(url: URL)

  fun navigateToTootDetails(id: String)

  companion object {
    internal val empty =
      object : ProfileDetailsBoundary {
        override fun navigateTo(url: URL) {}

        override fun navigateToTootDetails(id: String) {}
      }
  }
}
