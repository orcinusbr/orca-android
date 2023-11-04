package com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.reblog

import com.jeanbarrossilva.orca.core.feed.profile.toot.reblog.Reblog
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.reblog.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Reblog] returned by [sample]. */
private val testSampleReblog = Reblog.createSample(TestSampleImageLoader.Provider)

/** Test sample [Reblog]. */
val Reblog.Companion.sample
  get() = testSampleReblog
