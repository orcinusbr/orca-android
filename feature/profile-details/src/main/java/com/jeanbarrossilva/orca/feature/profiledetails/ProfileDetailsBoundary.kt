package com.jeanbarrossilva.orca.feature.profiledetails

import java.net.URL

interface ProfileDetailsBoundary {
  fun navigateTo(url: URL)

  fun navigateToPostDetails(id: String)

  companion object {
    internal val empty =
      object : ProfileDetailsBoundary {
        override fun navigateTo(url: URL) {}

        override fun navigateToPostDetails(id: String) {}
      }
  }
}
