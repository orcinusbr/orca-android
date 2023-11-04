package com.jeanbarrossilva.orca.core.sample.test.instance

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.SampleInstance
import com.jeanbarrossilva.orca.core.sample.instance.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.samples
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [SampleInstance] returned by [sample]. */
private val testSampleInstance =
  Instance.createSample(TestSampleImageLoader.Provider, defaultToots = Toot.samples)

/** Test [SampleInstance]. */
val Instance.Companion.sample
  get() = testSampleInstance
