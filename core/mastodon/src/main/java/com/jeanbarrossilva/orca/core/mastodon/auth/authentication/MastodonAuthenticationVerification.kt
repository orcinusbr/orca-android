package com.jeanbarrossilva.orca.core.mastodon.auth.authentication

import kotlinx.serialization.Serializable

/**
 * Structure returned by the API when the user's credentials are verified.
 *
 * @param id Unique identifier of the user.
 */
@Serializable internal data class MastodonAuthenticationVerification(val id: String)
