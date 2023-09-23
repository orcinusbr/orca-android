package com.jeanbarrossilva.orca.core.mastodon.social.instance

import android.content.Context
import com.jeanbarrossilva.orca.core.auth.actor.ActorProvider
import com.jeanbarrossilva.orca.core.http.instance.ContextualHttpInstance
import com.jeanbarrossilva.orca.core.instance.domain.Domain

class MastodonSocialInstance(context: Context, actorProvider: ActorProvider) :
    ContextualHttpInstance(context, actorProvider, Domain("mastodon.social"))
