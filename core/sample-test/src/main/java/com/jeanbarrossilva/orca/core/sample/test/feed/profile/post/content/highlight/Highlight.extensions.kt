package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Highlight] returned by [sample]. */
private val testSampleHighlight = Highlight.createSample(TestSampleImageLoader.Provider)

/** Test sample [Highlight]. */
val Highlight.Companion.sample
  get() = testSampleHighlight
