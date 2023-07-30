package com.jeanbarrossilva.mastodonte.core.mastodon.profile.toot

import com.jeanbarrossilva.mastodonte.core.mastodon.profile.toot.status.Status

internal data class Context(val ancestors: List<Status>, val descendants: List<Status>)
