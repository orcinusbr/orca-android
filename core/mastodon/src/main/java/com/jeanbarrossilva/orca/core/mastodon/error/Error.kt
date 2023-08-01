package com.jeanbarrossilva.orca.core.mastodon.error

import kotlinx.serialization.Serializable

@Serializable
internal data class Error(val error: String)
