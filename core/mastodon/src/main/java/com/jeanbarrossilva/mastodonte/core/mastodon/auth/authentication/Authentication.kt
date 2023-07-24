package com.jeanbarrossilva.mastodonte.core.mastodon.auth.authentication

import kotlinx.serialization.Serializable

@Serializable
internal data class Authentication(val accessToken: String)
