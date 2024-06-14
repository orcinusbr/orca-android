/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.instance

import android.content.Context
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.TermMuter
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.MastodonDatabase
import br.com.orcinus.orca.core.mastodon.auth.authentication.MastodonAuthenticator
import br.com.orcinus.orca.core.mastodon.auth.authorization.MastodonAuthorizer
import br.com.orcinus.orca.core.mastodon.feed.MastodonFeedPaginator
import br.com.orcinus.orca.core.mastodon.feed.MastodonFeedProvider
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfileProvider
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.MastodonProfileFetcher
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.MastodonProfileStorage
import br.com.orcinus.orca.core.mastodon.feed.profile.post.MastodonPost
import br.com.orcinus.orca.core.mastodon.feed.profile.post.cache.MastodonPostFetcher
import br.com.orcinus.orca.core.mastodon.feed.profile.post.cache.storage.MastodonPostStorage
import br.com.orcinus.orca.core.mastodon.feed.profile.post.provider.MastodonPostProvider
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.search.MastodonProfileSearcher
import br.com.orcinus.orca.core.mastodon.feed.profile.search.cache.MastodonProfileSearchResultsFetcher
import br.com.orcinus.orca.core.mastodon.feed.profile.search.cache.storage.MastodonProfileSearchResultsStorage
import br.com.orcinus.orca.platform.cache.Cache
import br.com.orcinus.orca.platform.cache.Fetcher
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

/**
 * [MastodonInstance] that authorizes the user and caches all fetched structures through the given
 * [Context].
 *
 * @param context [Context] with which the [database], [Cache]s and [Fetcher]s will be created.
 * @param actorProvider [ActorProvider] that will provide [Actor]s to the [authenticator], the
 *   [authenticationLock] and the [feedProvider].
 * @param termMuter [TermMuter] by which [Post]s with muted terms will be filtered out.
 * @property domain Unique identifier of the server.
 * @property authorizer [MastodonAuthorizer] by which the user will be authorized.
 * @property imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   images will be loaded.
 */
internal class ContextualMastodonInstance(
  context: Context,
  domain: Domain,
  authorizer: MastodonAuthorizer,
  override val authenticator: MastodonAuthenticator,
  actorProvider: ActorProvider,
  override val authenticationLock: AuthenticationLock<MastodonAuthenticator>,
  termMuter: TermMuter,
  internal val imageLoaderProvider: SomeImageLoaderProvider<URI>
) : MastodonInstance<MastodonAuthorizer, MastodonAuthenticator>(domain, authorizer) {
  /** [MastodonDatabase] in which cached structures will be persisted. */
  private val database = MastodonDatabase.getInstance(context)

  /**
   * [MastodonCommentPaginator.Provider] that provides the [MastodonProfilePostPaginator] to be used
   * by [postFetcher], [feedPostPaginator], [profilePostPaginatorProvider] and [postStorage].
   */
  private val commentPaginatorProvider =
    MastodonCommentPaginator.Provider {
      MastodonCommentPaginator(context, requester, imageLoaderProvider, it)
    }

  /** [MastodonPostFetcher] by which [Post]s will be fetched from the API. */
  private val postFetcher =
    MastodonPostFetcher(context, requester, imageLoaderProvider, commentPaginatorProvider)

  /**
   * [MastodonFeedPaginator] with which pagination through the feed's [Post]s that have been fetched
   * from the API will be performed.
   */
  private val feedPostPaginator =
    MastodonFeedPaginator(context, requester, imageLoaderProvider, commentPaginatorProvider)

  /**
   * [MastodonProfilePostPaginator.Provider] that provides the [MastodonProfilePostPaginator] to be
   * used by [profileFetcher], [profileStorage] and [profileSearchResultsFetcher].
   */
  private val profilePostPaginatorProvider =
    MastodonProfilePostPaginator.Provider {
      MastodonProfilePostPaginator(
        context,
        requester,
        imageLoaderProvider,
        commentPaginatorProvider,
        it
      )
    }

  /** [MastodonProfileFetcher] by which [MastodonProfile]s will be fetched from the API. */
  private val profileFetcher =
    MastodonProfileFetcher(context, requester, imageLoaderProvider, profilePostPaginatorProvider)

  /** [MastodonProfileStorage] that will store fetched [MastodonProfile]s. */
  private val profileStorage =
    MastodonProfileStorage(
      requester,
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
    MastodonProfileSearchResultsFetcher(
      context,
      requester,
      imageLoaderProvider,
      profilePostPaginatorProvider
    )

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
      requester,
      profileCache,
      database.postEntityDao,
      database.styleEntityDao,
      imageLoaderProvider,
      commentPaginatorProvider
    )

  /** [Cache] that decides how to obtain [MastodonPost]s. */
  private val postCache = Cache.of(context, name = "post-cache", postFetcher, postStorage)

  override val feedProvider = MastodonFeedProvider(actorProvider, termMuter, feedPostPaginator)
  override val profileProvider = MastodonProfileProvider(profileCache)
  override val profileSearcher = MastodonProfileSearcher(profileSearchResultsCache)
  override val postProvider = MastodonPostProvider(authenticationLock, postCache)
}
