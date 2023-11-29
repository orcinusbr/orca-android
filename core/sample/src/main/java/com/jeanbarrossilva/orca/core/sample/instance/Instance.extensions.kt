package com.jeanbarrossilva.orca.core.sample.instance

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSamples
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a [SampleInstance].
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s can be loaded from a [SampleImageSource].
 * @param defaultPosts [Post]s that are provided by default within the [SampleInstance].
 */
fun Instance.Companion.createSample(
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>,
  defaultPosts: List<Post> = Post.createSamples(imageLoaderProvider)
): SampleInstance {
  return SampleInstance(defaultPosts, imageLoaderProvider)
}
