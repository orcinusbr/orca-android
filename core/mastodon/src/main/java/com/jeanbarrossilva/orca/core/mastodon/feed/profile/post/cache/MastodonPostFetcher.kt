package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URL

/**
 * [Fetcher] that requests [Post]s to the API.
 *
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [URL].
 */
internal class MastodonPostFetcher(private val imageLoaderProvider: ImageLoader.Provider<URL>) :
  Fetcher<Post>() {
  override suspend fun onFetch(key: String): Post {
    return (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
      .client
      .authenticateAndGet("/api/v1/statuses/$key")
      .body<MastodonStatus>()
      .toPost(imageLoaderProvider)
  }
}
