package com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Highlight] returned by [sample]. */
private val testSampleHighlight = Highlight.createSample(TestSampleImageLoader.Provider)

/** Test sample [Highlight]. */
val Highlight.Companion.sample
  get() = testSampleHighlight
