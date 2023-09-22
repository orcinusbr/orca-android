package com.jeanbarrossilva.orca.app.module.core

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.ProfileProvider
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearcher
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.core.http.MastodonDatabase
import com.jeanbarrossilva.orca.core.http.auth.authentication.HttpAuthenticator
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizer
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfileProvider
import com.jeanbarrossilva.orca.core.http.feed.profile.ProfileTootPaginateSource
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.HttpProfileFetcher
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileStorage
import com.jeanbarrossilva.orca.core.http.feed.profile.search.HttpProfileSearcher
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.HttpProfileSearchResultsFetcher
import com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage.HttpProfileSearchResultsStorage
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.HttpTootProvider
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.HttpTootFetcher
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.HttpTootStorage
import com.jeanbarrossilva.orca.core.mastodon.social.MastodonSocialHttpClient
import com.jeanbarrossilva.orca.core.mastodon.social.mastodonSocialBaseURL
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.orca.platform.cache.Cache
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Suppress("FunctionName")
internal fun MainCoreModule(): Module {
    val context = KoinJavaComponent.get<Context>(Context::class.java)
    val authorizer = HttpAuthorizer(context)
    val actorProvider = SharedPreferencesActorProvider(context)
    val database = MastodonDatabase.getInstance(context)
    val profileTootPaginateSourceProvider =
        ProfileTootPaginateSource.Provider(::ProfileTootPaginateSource)
    val profileFetcher = HttpProfileFetcher(profileTootPaginateSourceProvider)
    val profileStorage =
        HttpProfileStorage(profileTootPaginateSourceProvider, database.profileEntityDao)
    val profileCache = Cache.of(context, name = "profile-cache", profileFetcher, profileStorage)
    val profileSearchResultsFetcher = HttpProfileSearchResultsFetcher(
        profileTootPaginateSourceProvider
    )
    val profileSearchResultsStorage =
        HttpProfileSearchResultsStorage(database.profileSearchResultEntityDao)
    val profileSearchResultsCache = Cache.of(
        context,
        name = "profile-search-results-cache",
        profileSearchResultsFetcher,
        profileSearchResultsStorage
    )
    val tootStorage =
        HttpTootStorage(profileCache, database.tootEntityDao, database.styleEntityDao)
    val tootCache = Cache.of(context, name = "toot-cache", HttpTootFetcher, tootStorage)
    return CoreModule(
        { authorizer },
        { HttpAuthenticator(context, authorizer, actorProvider) },
        { AuthenticationLock(authenticator = get(), actorProvider) },
        { com.jeanbarrossilva.orca.core.http.feed.HttpFeedProvider(actorProvider) },
        { HttpProfileProvider(profileCache) },
        { HttpProfileSearcher(profileSearchResultsCache) },
        { HttpTootProvider(tootCache) }
    )
        .apply {
            single { mastodonSocialBaseURL }
            single { MastodonSocialHttpClient }
        }
}

@Suppress("FunctionName")
internal fun CoreModule(
    authenticator: Definition<Authenticator>,
    authenticationLock: Definition<AuthenticationLock>,
    feedProvider: Definition<FeedProvider>,
    profileProvider: Definition<ProfileProvider>,
    profileSearcher: Definition<ProfileSearcher>,
    tootProvider: Definition<TootProvider>
): Module {
    return CoreModule<Authorizer, Authenticator>(
        authorizer = null,
        authenticator,
        authenticationLock,
        feedProvider,
        profileProvider,
        profileSearcher,
        tootProvider
    )
}

@Suppress("FunctionName")
private inline fun <reified A1 : Authorizer, reified A2 : Authenticator> CoreModule(
    noinline authorizer: Definition<A1>?,
    noinline authenticator: Definition<A2>,
    noinline authenticationLock: Definition<AuthenticationLock>,
    noinline feedProvider: Definition<FeedProvider>,
    noinline profileProvider: Definition<ProfileProvider>,
    noinline profileSearcher: Definition<ProfileSearcher>,
    noinline tootProvider: Definition<TootProvider>
): Module {
    return module {
        authorizer?.let { single(definition = it) binds arrayOf(Authorizer::class, A1::class) }
        single(definition = authenticator) binds arrayOf(Authenticator::class, A2::class)
        single(definition = authenticationLock)
        single(definition = feedProvider)
        single(definition = profileProvider)
        single(definition = profileSearcher)
        single(definition = tootProvider)
    }
}
