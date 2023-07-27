package com.jeanbarrossilva.mastodonte.app

import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.core.auth.Authenticator
import com.jeanbarrossilva.mastodonte.core.auth.Authorizer
import com.jeanbarrossilva.mastodonte.core.auth.actor.ActorProvider
import com.jeanbarrossilva.mastodonte.core.feed.FeedProvider
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication.MastodonAuthenticator
import com.jeanbarrossilva.mastodonte.core.mastodon.auth.authorization.MastodonAuthorizer
import com.jeanbarrossilva.mastodonte.core.profile.ProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.feed.SampleFeedProvider
import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileProvider
import com.jeanbarrossilva.mastodonte.core.sample.toot.SampleTootProvider
import com.jeanbarrossilva.mastodonte.core.sharedpreferences.actor.SharedPreferencesActorProvider
import com.jeanbarrossilva.mastodonte.core.toot.TootProvider
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.binds
import org.koin.dsl.module

@Suppress("FunctionName")
internal fun MastodonteModule(
    onBottomAreaAvailabilityChangeListener: OnBottomAreaAvailabilityChangeListener
): Module {
    return module {
        single<ActorProvider> { SharedPreferencesActorProvider(androidContext()) }
        single { MastodonAuthorizer(androidContext()) }
            .binds(arrayOf(Authorizer::class, MastodonAuthorizer::class))
        single<Authenticator> {
            MastodonAuthenticator(androidContext(), authorizer = get(), actorProvider = get())
        }
            .binds(arrayOf(Authenticator::class, MastodonAuthenticator::class))
        single { AuthenticationLock(authenticator = get(), actorProvider = get()) }
        single<FeedProvider> { SampleFeedProvider }
        single<ProfileProvider> { SampleProfileProvider }
        single<TootProvider> { SampleTootProvider }
        single { onBottomAreaAvailabilityChangeListener }
    }
}
