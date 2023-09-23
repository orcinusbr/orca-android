package com.jeanbarrossilva.orca.core.mastodon.social.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.client.CoreHttpClient
import com.jeanbarrossilva.orca.core.http.instance.ContextualHttpInstance
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.Url
import io.ktor.http.takeFrom

class MastodonSocialInstance(context: Context, actorProvider: ActorProvider) :
    ContextualHttpInstance(context, actorProvider) {
    override val url = Url("https://mastodon.social")
    override val client = CoreHttpClient {
        defaultRequest {
            url.takeFrom(this@MastodonSocialInstance.url)
        }
    }
}
