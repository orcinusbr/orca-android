package com.jeanbarrossilva.orca.core.sample.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.createSample
import com.jeanbarrossilva.orca.core.sample.image.SampleImageSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader

/**
 * Creates a sample [ProfileSearchResult].
 *
 * @param avatarImageLoader [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [ProfileSearchResult]'s avatar will be loaded.
 */
fun ProfileSearchResult.Companion.createSample(
  avatarImageLoader: ImageLoader.Provider<SampleImageSource>
): ProfileSearchResult {
  val author = Author.createSample(avatarImageLoader)
  return ProfileSearchResult(
    author.id,
    author.account,
    author.avatarLoader,
    author.name,
    author.profileURL
  )
}
