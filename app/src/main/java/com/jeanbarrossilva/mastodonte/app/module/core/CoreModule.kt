package com.jeanbarrossilva.mastodonte.app.module.core

import android.content.Context
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
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
import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Suppress("FunctionName")
internal fun MainCoreModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    val context = KoinJavaComponent.get<Context>(Context::class.java)
    val actorProvider = SharedPreferencesActorProvider(context)
    val authorizer = MastodonAuthorizer(context)
    val tootPaginateSource = MastodonFeedProvider.PaginateSource()
    val database = MastodonDatabase.getInstance(context)
    val profileStore = MastodonProfileStore(tootPaginateSource, database.profileEntityDao)
    return CoreModule(
        { MastodonAuthenticator(context, authorizer, actorProvider) },
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
    return module {
        single(definition = authenticator)
        single(definition = authenticationLock)
        single(definition = feedProvider)
        single(definition = profileProvider)
        single(definition = tootProvider)
        single(definition = onBottomAreaAvailabilityChangeListener)
    }
}
