package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSamples
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Post] returned by [sample]. */
private val testSamplePost = Post.createSample(TestSampleImageLoader.Provider)

/** [Post]s returned by [samples]. */
private val testSamplePosts = Post.createSamples(TestSampleImageLoader.Provider)

/** Test sample [Post]. */
val Post.Companion.sample
  get() = testSamplePost

/** Test sample [Post]s. */
val Post.Companion.samples
  get() = testSamplePosts
