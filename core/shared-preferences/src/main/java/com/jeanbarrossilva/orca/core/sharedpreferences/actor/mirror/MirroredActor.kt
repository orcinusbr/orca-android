/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.sharedpreferences.actor.mirror

import com.jeanbarrossilva.orca.core.auth.actor.Actor
import kotlinx.serialization.Serializable

@Serializable
internal class MirroredActor
private constructor(private val type: String, val id: String?, val accessToken: String?) {
  init {
    ensureIntegrity()
  }

  fun toActor(): Actor {
    return fold(
      onUnauthenticated = { Actor.Unauthenticated },
      onAuthenticated = { Actor.Authenticated(id!!, accessToken!!) }
    )
  }

  private fun ensureIntegrity() {
    fold(
      onUnauthenticated = ::ensureUnauthenticatedIntegrity,
      onAuthenticated = ::ensureAuthenticatedIntegrity
    )
  }

  private fun <T> fold(onUnauthenticated: () -> T, onAuthenticated: () -> T): T {
    return when (type) {
      UNAUTHENTICATED_TYPE -> onUnauthenticated()
      AUTHENTICATED_TYPE -> onAuthenticated()
      else -> throw IllegalArgumentException("Unknown \"$type\" mirrored actor type.")
    }
  }

  private fun ensureUnauthenticatedIntegrity() {
    require(id == null) { "$UNAUTHENTICATED_ERROR_MESSAGE_PREFIX an ID." }
    require(accessToken == null) { "$UNAUTHENTICATED_ERROR_MESSAGE_PREFIX an access token." }
  }

  private fun ensureAuthenticatedIntegrity() {
    require(id != null) { "$AUTHENTICATED_ERROR_MESSAGE_PREFIX an ID." }
    require(accessToken != null) { "$AUTHENTICATED_ERROR_MESSAGE_PREFIX an access token." }
  }

  companion object {
    private const val UNAUTHENTICATED_TYPE = "unauthenticated"
    private const val UNAUTHENTICATED_ERROR_MESSAGE_PREFIX =
      "Unauthenticated mirrored actor shouldn't have"
    private const val AUTHENTICATED_TYPE = "authenticated"
    private const val AUTHENTICATED_ERROR_MESSAGE_PREFIX =
      "Authenticated mirrored actor should have"

    fun unauthenticated(): MirroredActor {
      return MirroredActor(UNAUTHENTICATED_TYPE, id = null, accessToken = null)
    }

    fun authenticated(id: String, accessToken: String): MirroredActor {
      return MirroredActor(AUTHENTICATED_TYPE, id, accessToken)
    }
  }
}
