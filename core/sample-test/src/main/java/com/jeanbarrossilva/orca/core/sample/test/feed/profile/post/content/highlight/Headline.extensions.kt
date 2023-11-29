package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Headline] returned by [sample]. */
private val testSampleHeadline = Headline.createSample(TestSampleImageLoader.Provider)

/** Test sample [Headline]. */
val Headline.Companion.sample
  get() = testSampleHeadline
