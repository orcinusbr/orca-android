package com.jeanbarrossilva.orca.app.module.feature.tootdetails

import android.content.Context
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsBoundary
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.browseTo
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import java.net.URL

internal class NavigatorTootDetailsBoundary(
  private val context: Context,
  private val navigator: Navigator
) : TootDetailsBoundary {
  override fun navigateTo(url: URL) {
    context.browseTo(url)
  }

  override fun navigateToTootDetails(id: String) {
    TootDetailsFragment.navigate(navigator, id)
  }

  override fun pop() {
    navigator.pop()
  }
}
