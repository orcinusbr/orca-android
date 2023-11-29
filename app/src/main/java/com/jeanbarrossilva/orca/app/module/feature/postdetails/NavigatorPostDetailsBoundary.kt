package com.jeanbarrossilva.orca.app.module.feature.postdetails

import android.content.Context
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsBoundary
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.browseTo
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import java.net.URL

internal class NavigatorPostDetailsBoundary(
  private val context: Context,
  private val navigator: Navigator
) : PostDetailsBoundary {
  override fun navigateTo(url: URL) {
    context.browseTo(url)
  }

  override fun navigateToPostDetails(id: String) {
    PostDetailsFragment.navigate(navigator, id)
  }

  override fun pop() {
    navigator.pop()
  }
}
