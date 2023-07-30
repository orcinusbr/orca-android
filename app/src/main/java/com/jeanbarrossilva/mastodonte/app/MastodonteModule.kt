package com.jeanbarrossilva.mastodonte.app

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
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent

@Suppress("FunctionName")
internal fun MastodonteModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    val context = KoinJavaComponent.get<Context>(Context::class.java)
    val actorProvider = SharedPreferencesActorProvider(context)
    val authorizer = MastodonAuthorizer(context)
    val tootPaginateSource = MastodonFeedProvider.PaginateSource()
    val database = MastodonDatabase.getInstance(context)
    val profileStore = MastodonProfileStore(tootPaginateSource, database.profileEntityDao)
    return module {
        single<Authenticator> { MastodonAuthenticator(context, authorizer, actorProvider) }
        single { AuthenticationLock(authenticator = get(), actorProvider) }
        single<FeedProvider> { MastodonFeedProvider(actorProvider, tootPaginateSource) }
        single<ProfileProvider> { MastodonProfileProvider(profileStore) }
        single<TootProvider> { MastodonTootProvider() }
        single { onBottomAreaAvailabilityChangeListener }
    }
}
