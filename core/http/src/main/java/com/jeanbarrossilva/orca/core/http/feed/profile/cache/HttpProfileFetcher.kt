package com.jeanbarrossilva.orca.core.http.feed.profile.cache

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.client.authenticateAndGet
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginator
import com.jeanbarrossilva.orca.core.http.feed.profile.account.HttpAccount
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.module.instanceProvider
import com.jeanbarrossilva.orca.platform.cache.Fetcher
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import com.jeanbarrossilva.orca.std.injector.Injector
import io.ktor.client.call.body
import java.net.URL

/**
 * [Fetcher] for [HttpProfile]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Profile]s' avatars will be loaded from a [URL].
 * @param tootPaginatorProvider [ProfileTootPaginator.Provider] by which a [ProfileTootPaginator]
 *   for paginating through an [HttpProfile]'s [Toot]s will be provided.
 */
internal class HttpProfileFetcher(
  private val avatarLoaderProvider: ImageLoader.Provider<URL>,
  private val tootPaginatorProvider: ProfileTootPaginator.Provider
) : Fetcher<Profile>() {
  override suspend fun onFetch(key: String): Profile {
    return (Injector.from<CoreModule>().instanceProvider().provide() as SomeHttpInstance)
      .client
      .authenticateAndGet("/api/v1/accounts/$key")
      .body<HttpAccount>()
      .toProfile(avatarLoaderProvider, tootPaginatorProvider)
  }
}
