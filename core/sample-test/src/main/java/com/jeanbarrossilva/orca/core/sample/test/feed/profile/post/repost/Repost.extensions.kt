package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.repost

import com.jeanbarrossilva.orca.core.feed.profile.post.repost.Repost
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.repost.createSample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight.sample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Repost] returned by [sample]. */
private val testSampleRepost = Repost.createSample(TestSampleImageLoader.Provider)

/** Test sample [Repost]. */
val Repost.Companion.sample
  get() = testSampleRepost
