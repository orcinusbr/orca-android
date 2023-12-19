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

package com.jeanbarrossilva.orca.core.mastodon.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.TermMuter
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.mastodon.MastodonDatabase
import com.jeanbarrossilva.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.orca.core.mastodon.feed.MastodonFeedPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.MastodonFeedProvider
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfileProvider
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.MastodonProfileFetcher
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.MastodonProfileStorage
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPost
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.MastodonPostProvider
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.MastodonPostFetcher
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage.MastodonPostStorage
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.MastodonProfileSearcher
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.MastodonProfileSearchResultsFetcher
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage.MastodonProfileSearchResultsStorage
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.std.image.ImageLoader
import com.jeanbarrossilva.orca.std.image.SomeImageLoaderProvider
import java.net.URL

/**
 * [MastodonInstance] that authorizes the user and caches all fetched structures through the given
 * [Context].
 *
 * @param context [Context] with which the [authenticator] will be created.
 * @param domain Unique identifier of the server.
 * @param authorizer [MastodonAuthorizer] by which the user will be authorized.
 * @param actorProvider [ActorProvider] that will provide [Actor]s to the [authenticator], the
 *   [authenticationLock] and the [feedProvider].
 * @param termMuter [TermMuter] by which [Post]s with muted terms will be filtered out.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which images
 *   will be loaded.
 */
class ContextualMastodonInstance(
  context: Context,
  domain: Domain,
  authorizer: MastodonAuthorizer,
  override val authenticator: MastodonAuthenticator,
  actorProvider: ActorProvider,
  override val authenticationLock: AuthenticationLock<MastodonAuthenticator>,
  termMuter: TermMuter,
  imageLoaderProvider: SomeImageLoaderProvider<URL>
) : MastodonInstance<MastodonAuthorizer, MastodonAuthenticator>(domain, authorizer) {
  /** [MastodonDatabase] in which cached structures will be persisted. */
  private val database = MastodonDatabase.getInstance(context)

  /** [MastodonPostFetcher] by which [Post]s will be fetched from the API. */
  private val postFetcher = MastodonPostFetcher(imageLoaderProvider)

  /**
   * [MastodonFeedPaginator] with which pagination through the feed's [Post]s that have been fetched
   * from the API will be performed.
   */
  private val feedPostPaginator = MastodonFeedPaginator(imageLoaderProvider)

  /**
   * [MastodonProfilePostPaginator.Provider] that provides the [MastodonProfilePostPaginator] to be
   * used by [profileFetcher], [profileStorage] and [profileSearchResultsFetcher].
   */
  private val profilePostPaginatorProvider =
    MastodonProfilePostPaginator.Provider { MastodonProfilePostPaginator(imageLoaderProvider, it) }

  /** [MastodonProfileFetcher] by which [MastodonProfile]s will be fetched from the API. */
  private val profileFetcher =
    MastodonProfileFetcher(imageLoaderProvider, profilePostPaginatorProvider)

  /** [MastodonProfileStorage] that will store fetched [MastodonProfile]s. */
  private val profileStorage =
    MastodonProfileStorage(
      imageLoaderProvider,
      profilePostPaginatorProvider,
      database.profileEntityDao
    )

  /** [Cache] that decides how to obtain [MastodonProfile]s. */
  private val profileCache =
    Cache.of(context, name = "profile-cache", profileFetcher, profileStorage)

  /**
   * [MastodonProfileSearchResultsFetcher] by which [ProfileSearchResult]s will be fetched from the
   * API.
   */
  private val profileSearchResultsFetcher =
    MastodonProfileSearchResultsFetcher(imageLoaderProvider, profilePostPaginatorProvider)

  /** [MastodonProfileSearchResultsStorage] that will store fetched [ProfileSearchResult]s. */
  private val profileSearchResultsStorage =
    MastodonProfileSearchResultsStorage(imageLoaderProvider, database.profileSearchResultEntityDao)

  /** [Cache] that decides how to obtain [ProfileSearchResult]s. */
  private val profileSearchResultsCache =
    Cache.of(
      context,
      name = "profile-search-results-cache",
      profileSearchResultsFetcher,
      profileSearchResultsStorage
    )

  /** [MastodonPostStorage] that will store fetched [MastodonPost]s. */
  private val postStorage =
    MastodonPostStorage(
      profileCache,
      database.postEntityDao,
      database.styleEntityDao,
      imageLoaderProvider
    )

  /** [Cache] that decides how to obtain [MastodonPost]s. */
  private val postCache = Cache.of(context, name = "post-cache", postFetcher, postStorage)

  override val feedProvider = MastodonFeedProvider(actorProvider, termMuter, feedPostPaginator)
  override val profileProvider = MastodonProfileProvider(profileCache)
  override val profileSearcher = MastodonProfileSearcher(profileSearchResultsCache)
  override val postProvider = MastodonPostProvider(postCache)
}
