package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Author] returned by [sample]. */
private val testSampleAuthor = Author.createSample(TestSampleImageLoader.Provider)

/** Test sample [Author]. */
val Author.Companion.sample
  get() = testSampleAuthor
