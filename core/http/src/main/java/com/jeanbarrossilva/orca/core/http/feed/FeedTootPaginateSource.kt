package com.jeanbarrossilva.orca.core.http.feed

import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.pagination.HttpTootPaginateSource
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/** [HttpTootPaginateSource] that paginates through [HttpToot]s of the feed. */
internal class FeedTootPaginateSource(override val imageLoaderProvider: ImageLoader.Provider<URL>) :
  HttpTootPaginateSource() {
  override val route = "/api/v1/timelines/home"
}
