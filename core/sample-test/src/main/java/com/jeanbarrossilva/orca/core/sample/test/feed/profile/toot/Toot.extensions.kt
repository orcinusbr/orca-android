package com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSamples
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Toot] returned by [sample]. */
private val testSampleToot = Toot.createSample(TestSampleImageLoader.Provider)

/** [Toot]s returned by [samples]. */
private val testSampleToots = Toot.createSamples(TestSampleImageLoader.Provider)

/** Test sample [Toot]. */
val Toot.Companion.sample
  get() = testSampleToot

/** Test sample [Toot]s. */
val Toot.Companion.samples
  get() = testSampleToots
