package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonContext
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URL
import kotlinx.coroutines.flow.flow

/**
 * Builds a [Stat] for an [MastodonPost]'s comments that obtains them from the API.
 *
 * @param id ID of the [MastodonPost] for which the [Stat] is.
 * @param count Amount of comments that the [MastodonPost] has received.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded from a [URL].
 */
@Suppress("FunctionName")
internal fun CommentStat(
  id: String,
  count: Int,
  imageLoaderProvider: ImageLoader.Provider<URL>
): Stat<Post> {
  return Stat(count) {
    get {
      flow {
        (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
          .client
          .authenticateAndGet("/api/v1/statuses/$id/context")
          .body<MastodonContext>()
          .descendants
          .map { it.toPost(imageLoaderProvider) }
          .also { emit(it) }
      }
    }
  }
}
