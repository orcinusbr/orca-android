package com.jeanbarrossilva.orca.feature.postdetails

import java.net.URL

interface PostDetailsBoundary {
  fun navigateTo(url: URL)

  fun navigateToPostDetails(id: String)

  fun pop()
}
