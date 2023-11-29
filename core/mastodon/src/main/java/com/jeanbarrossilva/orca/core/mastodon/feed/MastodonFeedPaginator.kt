package com.jeanbarrossilva.orca.core.mastodon.feed

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination.MastodonPostPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/** [MastodonPostPaginator] that paginates through [MastodonPost]s of the feed. */
internal class MastodonFeedPaginator(override val imageLoaderProvider: ImageLoader.Provider<URL>) :
  MastodonPostPaginator() {
  override val route = "/api/v1/timelines/home"
}
