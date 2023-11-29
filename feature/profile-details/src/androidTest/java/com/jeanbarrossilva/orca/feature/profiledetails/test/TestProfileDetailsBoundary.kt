package com.jeanbarrossilva.orca.feature.profiledetails.test

import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import java.net.URL

internal class TestProfileDetailsBoundary : ProfileDetailsBoundary {
  override fun navigateTo(url: URL) {}

  override fun navigateToPostDetails(id: String) {}
}
