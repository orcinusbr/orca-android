package com.jeanbarrossilva.orca.core.sample.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootProvider
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/**
 * Creates a sample [Profile].
 *
 * @param tootProvider [SampleTootProvider] by which the [Profile]'s [Toot]s will be provided.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Image]s will be loaded from a [SampleImageSource].
 */
fun Profile.Companion.createSample(
  tootProvider: SampleTootProvider,
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
    override val tootProvider = tootProvider
  }
}
