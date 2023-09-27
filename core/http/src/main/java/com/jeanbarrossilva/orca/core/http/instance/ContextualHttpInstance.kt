package com.jeanbarrossilva.orca.core.http.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.http.MastodonDatabase
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
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

/**
 * [HttpInstance] that authorizes the user and caches all fetched structures through the given
 * [Context].
 *
 * @param context [Context] with which the [authenticator] will be created.
 * @param actorProvider [ActorProvider] that will provide [Actor]s to both the [authenticator] and
 * the [authenticationLock].
 * @param domain Unique identifier of the server.
 */
abstract class ContextualHttpInstance(
    context: Context,
    actorProvider: ActorProvider,
    domain: Domain
) : HttpInstance<HttpAuthorizer, HttpAuthenticator>(domain, HttpAuthorizer(context)) {
    /** [MastodonDatabase] in which cached structures will be persisted. */
    private val database = MastodonDatabase.getInstance(context)

    /**
     * [ProfileTootPaginateSource.Provider] that provides the [ProfileTootPaginateSource] to be used
     * by [profileFetcher], [profileStorage] and [profileSearchResultsFetcher].
     */
    private val profileTootPaginateSourceProvider =
        ProfileTootPaginateSource.Provider(::ProfileTootPaginateSource)

    /** [HttpProfileFetcher] by which [HttpProfile]s will be fetched from the API. */
    private val profileFetcher = HttpProfileFetcher(profileTootPaginateSourceProvider)

    /** [HttpProfileStorage] that will store fetched [HttpProfile]s. */
    private val profileStorage =
        HttpProfileStorage(profileTootPaginateSourceProvider, database.profileEntityDao)

    /** [Cache] that decides how to obtain [HttpProfile]s. */
    private val profileCache =
        Cache.of(context, name = "profile-cache", profileFetcher, profileStorage)

    /**
     * [HttpProfileSearchResultsFetcher] by which [ProfileSearchResult]s will be fetched from the
     * API.
     */
    private val profileSearchResultsFetcher =
        HttpProfileSearchResultsFetcher(profileTootPaginateSourceProvider)

    /** [HttpProfileSearchResultsStorage] that will store fetched [ProfileSearchResult]s. */
    private val profileSearchResultsStorage =
        HttpProfileSearchResultsStorage(database.profileSearchResultEntityDao)

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
        HttpTootStorage(profileCache, database.tootEntityDao, database.styleEntityDao)

    /** [Cache] that decides how to obtain [HttpToot]s. */
    private val tootCache = Cache.of(context, name = "toot-cache", HttpTootFetcher, tootStorage)

    final override val authenticator = HttpAuthenticator(context, authorizer, actorProvider)
    final override val authenticationLock = AuthenticationLock(authenticator, actorProvider)
    final override val feedProvider = HttpFeedProvider(actorProvider)
    final override val profileProvider = HttpProfileProvider(profileCache)
    final override val profileSearcher = HttpProfileSearcher(profileSearchResultsCache)
    final override val tootProvider = HttpTootProvider(tootCache)
}
