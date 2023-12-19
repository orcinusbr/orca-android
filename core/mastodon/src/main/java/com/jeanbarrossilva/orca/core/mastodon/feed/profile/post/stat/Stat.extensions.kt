/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.stat

import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.mastodon.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonContext
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.mastodon.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URL
import kotlinx.coroutines.flow.flow

/**
 * Builds a [Stat] for an [MastodonPost]'s comments that obtains them from the API.
 *
 * @param id ID of the [MastodonPost] for which the [Stat] is.
 * @param count Amount of comments that the [MastodonPost] has received.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded from a [URL].
 */
@Suppress("FunctionName")
internal fun CommentStat(
  id: String,
  count: Int,
  imageLoaderProvider: SomeImageLoaderProvider<URL>
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
