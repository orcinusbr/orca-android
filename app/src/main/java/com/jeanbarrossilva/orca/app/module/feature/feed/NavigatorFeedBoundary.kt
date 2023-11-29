package com.jeanbarrossilva.orca.app.module.feature.feed

import android.content.Context
import com.jeanbarrossilva.orca.feature.composer.ComposerActivity
import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.feature.postdetails.PostDetailsFragment
import com.jeanbarrossilva.orca.feature.search.SearchFragment
import com.jeanbarrossilva.orca.platform.ui.core.browseTo
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import java.net.URL

internal class NavigatorFeedBoundary(
  private val context: Context,
  private val navigator: Navigator
) : FeedBoundary {
  override fun navigateToSearch() {
    SearchFragment.navigate(navigator)
  }

  override fun navigateTo(url: URL) {
    context.browseTo(url)
  }

  override fun navigateToPostDetails(id: String) {
    PostDetailsFragment.navigate(navigator, id)
  }

  override fun navigateToComposer() {
    ComposerActivity.start(context)
  }
}
