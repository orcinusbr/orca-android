package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot

import kotlinx.serialization.Serializable

@Serializable
internal data class Context(val ancestors: List<Status>, val descendants: List<Status>)
