package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot

import com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status.Status

internal data class Context(val ancestors: List<Status>, val descendants: List<Status>)
