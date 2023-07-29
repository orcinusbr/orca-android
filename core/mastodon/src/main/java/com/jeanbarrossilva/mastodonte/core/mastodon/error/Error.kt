package com.jeanbarrossilva.mastodonte.core.mastodon.error

import kotlinx.serialization.Serializable

@Serializable
internal data class Error(val error: String)
