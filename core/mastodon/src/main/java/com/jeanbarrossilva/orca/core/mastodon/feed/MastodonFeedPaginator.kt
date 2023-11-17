package com.jeanbarrossilva.orca.core.mastodon.feed

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.MastodonToot
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.pagination.MastodonTootPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/** [MastodonTootPaginator] that paginates through [MastodonToot]s of the feed. */
internal class MastodonFeedPaginator(override val imageLoaderProvider: ImageLoader.Provider<URL>) :
  MastodonTootPaginator() {
  override val route = "/api/v1/timelines/home"
}
