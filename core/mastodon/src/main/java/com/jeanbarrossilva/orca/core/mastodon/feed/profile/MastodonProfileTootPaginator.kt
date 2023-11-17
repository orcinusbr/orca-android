package com.jeanbarrossilva.orca.core.mastodon.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.pagination.MastodonTootPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [MastodonTootPaginator] that paginates through a [MastodonProfile]'s [Toot]s.
 *
 * @param id ID of the [MastodonProfile].
 */
internal class MastodonProfileTootPaginator(
  override val imageLoaderProvider: ImageLoader.Provider<URL>,
  id: String
) : MastodonTootPaginator() {
  override val route = "/api/v1/accounts/$id/statuses"

  /** Provides a [MastodonProfileTootPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [MastodonProfileTootPaginator] that paginates through the [Toot]s of a
     * [MastodonProfile] identified as [id].
     *
     * @param id ID of the [MastodonProfile].
     */
    fun provide(id: String): MastodonProfileTootPaginator
  }
}
