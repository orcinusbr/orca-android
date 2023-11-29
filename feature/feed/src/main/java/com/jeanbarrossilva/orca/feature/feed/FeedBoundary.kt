package com.jeanbarrossilva.orca.feature.feed

import java.net.URL

interface FeedBoundary {
  fun navigateToSearch()

  fun navigateTo(url: URL)

  fun navigateToPostDetails(id: String)

  fun navigateToComposer()
}
