package com.jeanbarrossilva.orca.core.http.feed.profile

import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [HttpTootPaginator] that paginates through an [HttpProfile]'s [Toot]s.
 *
 * @param id ID of the [HttpProfile].
 */
internal class ProfileTootPaginator(
  override val imageLoaderProvider: ImageLoader.Provider<URL>,
  id: String
) : HttpTootPaginator() {
  override val route = "/api/v1/accounts/$id/statuses"

  /** Provides a [ProfileTootPaginator] through [provide]. */
  fun interface Provider {
    /**
     * Provides a [ProfileTootPaginator] that paginates through the [Toot]s of an [HttpProfile]
     * identified as [id].
     *
     * @param id ID of the [HttpProfile].
     */
    fun provide(id: String): ProfileTootPaginator
  }
}
