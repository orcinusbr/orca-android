package com.jeanbarrossilva.orca.core.http.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.TermMuter
import com.jeanbarrossilva.orca.core.http.HttpDatabase
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.feed.FeedTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.HttpFeedProvider
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfileProvider
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.HttpProfileFetcher
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileStorage
import com.jeanbarrossilva.orca.core.http.feed.profile.search.HttpProfileSearcher
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.HttpProfileSearchResultsFetcher
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage.HttpProfileSearchResultsStorage
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpToot
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpTootProvider
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.HttpTootFetcher
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.HttpTootStorage
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.std.imageloader.Image
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [HttpInstance] that authorizes the user and caches all fetched structures through the given
 * [Context].
 *
 * @param context [Context] with which the [authenticator] will be created.
 * @param domain Unique identifier of the server.
 * @param authorizer [HttpAuthorizer] by which the user will be authorized.
 * @param actorProvider [ActorProvider] that will provide [Actor]s to the [authenticator], the
 *   [authenticationLock] and the [feedProvider].
 * @param termMuter [TermMuter] by which [Toot]s with muted terms will be filtered out.
 * @param imageLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   [Image]s will be loaded.
 */
class ContextualHttpInstance(
  context: Context,
  domain: Domain,
  authorizer: HttpAuthorizer,
  override val authenticator: HttpAuthenticator,
  actorProvider: ActorProvider,
  override val authenticationLock: AuthenticationLock<HttpAuthenticator>,
  termMuter: TermMuter,
  imageLoaderProvider: ImageLoader.Provider<URL>
) : HttpInstance<HttpAuthorizer, HttpAuthenticator>(domain, authorizer) {
  /** [HttpDatabase] in which cached structures will be persisted. */
  private val database = HttpDatabase.getInstance(context)

  /** [HttpTootFetcher] by which [Toot]s will be fetched from the API. */
  private val tootFetcher = HttpTootFetcher(imageLoaderProvider)

  /**
   * [FeedTootPaginateSource] with which pagination through the feed's [Toot]s that have been
   * fetched from the API will be performed.
   */
  private val feedTootPaginateSource = FeedTootPaginateSource(imageLoaderProvider)

  /**
   * [ProfileTootPaginateSource.Provider] that provides the [ProfileTootPaginateSource] to be used
   * by [profileFetcher], [profileStorage] and [profileSearchResultsFetcher].
   */
  private val profileTootPaginateSourceProvider =
    ProfileTootPaginateSource.Provider { ProfileTootPaginateSource(imageLoaderProvider, it) }

  /** [HttpProfileFetcher] by which [HttpProfile]s will be fetched from the API. */
  private val profileFetcher =
    HttpProfileFetcher(imageLoaderProvider, profileTootPaginateSourceProvider)

  /** [HttpProfileStorage] that will store fetched [HttpProfile]s. */
  private val profileStorage =
    HttpProfileStorage(
      imageLoaderProvider,
      profileTootPaginateSourceProvider,
      database.profileEntityDao
    )

  /** [Cache] that decides how to obtain [HttpProfile]s. */
  private val profileCache =
    Cache.of(context, name = "profile-cache", profileFetcher, profileStorage)

  /**
   * [HttpProfileSearchResultsFetcher] by which [ProfileSearchResult]s will be fetched from the API.
   */
  private val profileSearchResultsFetcher =
    HttpProfileSearchResultsFetcher(imageLoaderProvider, profileTootPaginateSourceProvider)

  /** [HttpProfileSearchResultsStorage] that will store fetched [ProfileSearchResult]s. */
  private val profileSearchResultsStorage =
    HttpProfileSearchResultsStorage(imageLoaderProvider, database.profileSearchResultEntityDao)

  /** [Cache] that decides how to obtain [ProfileSearchResult]s. */
  private val profileSearchResultsCache =
    Cache.of(
      context,
      name = "profile-search-results-cache",
      profileSearchResultsFetcher,
      profileSearchResultsStorage
    )

  /** [HttpTootStorage] that will store fetched [HttpToot]s. */
  private val tootStorage =
    HttpTootStorage(
      profileCache,
      database.tootEntityDao,
      database.styleEntityDao,
      imageLoaderProvider
    )

  /** [Cache] that decides how to obtain [HttpToot]s. */
  private val tootCache = Cache.of(context, name = "toot-cache", tootFetcher, tootStorage)

  override val feedProvider = HttpFeedProvider(actorProvider, termMuter, feedTootPaginateSource)
  override val profileProvider = HttpProfileProvider(profileCache)
  override val profileSearcher = HttpProfileSearcher(profileSearchResultsCache)
  override val tootProvider = HttpTootProvider(tootCache)
}
