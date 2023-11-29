package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [MastodonPostPaginator] that paginates through a [MastodonProfile]'s [Post]s.
 *
 * @param id ID of the [MastodonProfile].
 */
internal class MastodonProfilePostPaginator(
  override val imageLoaderProvider: ImageLoader.Provider<URL>,
  id: String
) : MastodonPostPaginator() {
  override val route = "/api/v1/accounts/$id/statuses"

  /** Provides a [MastodonProfilePostPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [MastodonProfilePostPaginator] that paginates through the [Post]s of a
     * [MastodonProfile] identified as [id].
     *
     * @param id ID of the [MastodonProfile].
     */
    fun provide(id: String): MastodonProfilePostPaginator
  }
}
