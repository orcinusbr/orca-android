package com.jeanbarrossilva.mastodonte.core.mastodon.toot

internal data class Context(val ancestors: List<Status>, val descendants: List<Status>)
