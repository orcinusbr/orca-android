package com.jeanbarrossilva.mastodonte.app

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.mastodonte.core.mastodon.feed.MastodonFeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.profile.MastodonProfileProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.MastodonTootProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.TootPaginateSource
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import java.util.UUID
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MastodonteModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    val tootPaginateSourceName = UUID.randomUUID().toString()
    return module {
        single<ActorProvider> { SharedPreferencesActorProvider(androidContext()) }
        single { MastodonAuthorizer(androidContext()) }
            .binds(arrayOf(Authorizer::class, MastodonAuthorizer::class))
        single<Authenticator> {
            MastodonAuthenticator(androidContext(), authorizer = get(), actorProvider = get())
        }
            .binds(arrayOf(Authenticator::class, MastodonAuthenticator::class))
        single { AuthenticationLock(authenticator = get(), actorProvider = get()) }
        single(named(tootPaginateSourceName)) { MastodonFeedProvider.PaginateSource() }
            .binds(arrayOf(TootPaginateSource::class, MastodonFeedProvider.PaginateSource::class))
        single<FeedProvider> {
            MastodonFeedProvider(
                actorProvider = get(),
                paginateSource = get(named(tootPaginateSourceName))
            )
        }
        single<ProfileProvider> { MastodonProfileProvider(get(named(tootPaginateSourceName))) }
        single<TootProvider> { MastodonTootProvider() }
        single { onBottomAreaAvailabilityChangeListener }
    }
}
