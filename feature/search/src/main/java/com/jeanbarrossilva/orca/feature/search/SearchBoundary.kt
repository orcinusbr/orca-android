package com.jeanbarrossilva.orca.feature.search

interface SearchBoundary {
  fun navigateToProfileDetails(id: String)

  fun pop()
}
