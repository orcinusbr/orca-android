package com.jeanbarrossilva.orca.feature.feed.test

import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import java.net.URL

internal class TestFeedBoundary : FeedBoundary {
  override fun navigateToSearch() {}

  override fun navigateTo(url: URL) {}

  override fun navigateToPostDetails(id: String) {}

  override fun navigateToComposer() {}
}
