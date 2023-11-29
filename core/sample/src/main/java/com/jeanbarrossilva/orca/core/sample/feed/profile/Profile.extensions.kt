package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.SamplePostProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/**
 * Creates a sample [Profile].
 *
 * @param postProvider [SamplePostProvider] by which the [Profile]'s [Post]s will be provided.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Profile.Companion.createSample(
  postProvider: SamplePostProvider,
  imageLoaderProvider: ImageLoader.Provider<SampleImageSource>
): Profile {
  val author = Author.createSample(imageLoaderProvider)
  return object : SampleProfile {
    override val id = author.id
    override val account = author.account
    override val avatarLoader = author.avatarLoader
    override val name = author.name
    override val bio =
      StyledString(
        "Co-founder @ Grupo Estoa, software engineer, author, writer and content creator; " +
          "neuroscience, quantum physics and philosophy enthusiast."
      )
    override val followerCount = 1_024
    override val followingCount = 64
    override val url = author.profileURL
    override val postProvider = postProvider
  }
}
