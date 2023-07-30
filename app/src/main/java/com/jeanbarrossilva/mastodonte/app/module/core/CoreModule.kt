package com.jeanbarrossilva.mastodonte.app.module.core

import android.content.Context
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.MastodonDatabase
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.MastodonFeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.MastodonProfileProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.cache.MastodonProfileStore
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.MastodonTootProvider
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.android.ext.koin.androidContext
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Suppress("FunctionName")
internal fun MainCoreModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    val context = KoinJavaComponent.get<Context>(Context::class.java)
    val actorProvider = SharedPreferencesActorProvider(context)
    val tootPaginateSource = MastodonFeedProvider.PaginateSource()
    val database = MastodonDatabase.getInstance(context)
    val profileStore = MastodonProfileStore(tootPaginateSource, database.profileEntityDao)
    return CoreModule(
        { MastodonAuthorizer(androidContext()) },
        { MastodonAuthenticator(context, authorizer = get(), actorProvider) },
        { AuthenticationLock(authenticator = get(), actorProvider) },
        { MastodonFeedProvider(actorProvider, tootPaginateSource) },
        { MastodonProfileProvider(profileStore) },
        { MastodonTootProvider() }
    ) {
        onBottomAreaAvailabilityChangeListener
    }
}

@Suppress("FunctionName")
internal fun CoreModule(
    authenticator: Definition<Authenticator>,
    authenticationLock: Definition<AuthenticationLock>,
    feedProvider: Definition<FeedProvider>,
    profileProvider: Definition<ProfileProvider>,
    tootProvider: Definition<TootProvider>,
    onBottomAreaAvailabilityChangeListener: Definition<OnBottomAreaAvailabilityChangeListener>
): Module {
    return CoreModule<Authorizer, Authenticator>(
        authorizer = null,
        authenticator,
        authenticationLock,
        feedProvider,
        profileProvider,
        tootProvider,
        onBottomAreaAvailabilityChangeListener
    )
}

@Suppress("FunctionName")
private inline fun <reified A1 : Authorizer, reified A2 : Authenticator> CoreModule(
    noinline authorizer: Definition<A1>?,
    noinline authenticator: Definition<A2>,
    noinline authenticationLock: Definition<AuthenticationLock>,
    noinline feedProvider: Definition<FeedProvider>,
    noinline profileProvider: Definition<ProfileProvider>,
    noinline tootProvider: Definition<TootProvider>,
    noinline onBottomAreaAvailabilityChangeListener:
        Definition<OnBottomAreaAvailabilityChangeListener>
): Module {
    return module {
        authorizer?.let { single(definition = it) binds arrayOf(Authorizer::class, A1::class) }
        single(definition = authenticator) binds arrayOf(Authenticator::class, A2::class)
        single(definition = authenticationLock)
        single(definition = feedProvider)
        single(definition = profileProvider)
        single(definition = tootProvider)
        single(definition = onBottomAreaAvailabilityChangeListener)
    }
}
