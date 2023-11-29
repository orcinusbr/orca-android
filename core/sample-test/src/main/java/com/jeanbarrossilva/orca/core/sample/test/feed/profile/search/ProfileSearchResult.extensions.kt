package com.jeanbarrossilva.orca.core.sample.test.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [ProfileSearchResult] returned by [sample]. */
private val testSampleProfileSearchResult =
  ProfileSearchResult.createSample(TestSampleImageLoader.Provider)

/** Test sample [ProfileSearchResult]. */
val ProfileSearchResult.Companion.sample
  get() = testSampleProfileSearchResult
