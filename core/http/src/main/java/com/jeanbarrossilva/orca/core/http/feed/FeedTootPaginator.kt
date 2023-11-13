package com.jeanbarrossilva.orca.core.http.feed

import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginator
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/** [HttpTootPaginator] that paginates through [HttpToot]s of the feed. */
internal class FeedTootPaginator(override val imageLoaderProvider: ImageLoader.Provider<URL>) :
  HttpTootPaginator() {
  override val route = "/api/v1/timelines/home"
}
