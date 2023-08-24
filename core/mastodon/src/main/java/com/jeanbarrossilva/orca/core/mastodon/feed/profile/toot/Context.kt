package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.status.Status
import kotlinx.serialization.Serializable

@Serializable
internal data class Context(val ancestors: List<Status>, val descendants: List<Status>)
