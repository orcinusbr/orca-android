package com.jeanbarrossilva.mastodonte.core.mastodon.toot

import com.jeanbarrossilva.mastodonte.core.mastodon.toot.status.Status

internal data class Context(val ancestors: List<Status>, val descendants: List<Status>)
