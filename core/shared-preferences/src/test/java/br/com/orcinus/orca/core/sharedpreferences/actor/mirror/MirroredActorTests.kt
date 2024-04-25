/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.sharedpreferences.actor.mirror

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.test.auth.actor.sample
import br.com.orcinus.orca.std.uri.URIBuilder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import org.junit.Test

internal class MirroredActorTests {
  @Test
  fun isSerializableWhenUnauthenticated() {
    val encoder = createJsonTreeEncoder()
    val actor = MirroredActor.unauthenticated()
    MirroredActor.serializer().serialize(encoder, actor)
  }

  @Test
  fun isSerializableWhenAuthenticatedWithSampleAvatarSource() {
    val encoder = createJsonTreeEncoder()
    val actor = Actor.Authenticated.sample.toMirroredActor()
    MirroredActor.serializer().serialize(encoder, actor)
  }

  @Test
  fun isSerializableWhenAuthenticatedWithAvatarURISource() {
    val encoder = createJsonTreeEncoder()
    val delegate = Actor.Authenticated.sample
    val avatarURI =
      URIBuilder.scheme("https")
        .host("app.orca.jeanbarrossilva.com")
        .path("profile")
        .path("@jeanbarrossilva")
        .path("avatar.jpg")
        .build()
    val actor = MirroredActor.authenticated(delegate.id, delegate.accessToken, avatarURI)
    MirroredActor.serializer().serialize(encoder, actor)
  }

  @Test
  fun isDeserializableWhenUnauthenticated() {
    val actor = MirroredActor.unauthenticated()
    val jsonObject = Json.encodeToJsonElement(actor).jsonObject
    val decoder = createJsonTreeDecoder(jsonObject)
    assertThat(MirroredActor.serializer().deserialize(decoder)).isEqualTo(actor)
  }

  @Test
  fun isDeserializableWhenAuthenticatedWithSampleAvatarSource() {
    val actor = Actor.Authenticated.sample.toMirroredActor()
    val jsonObject = Json.encodeToJsonElement(actor).jsonObject
    val decoder = createJsonTreeDecoder(jsonObject)
    assertThat(MirroredActor.serializer().deserialize(decoder)).isEqualTo(actor)
  }

  @Test
  fun isDeserializableWhenAuthenticatedWithAvatarURI() {
    val delegate = Actor.Authenticated.sample
    val avatarURI =
      URIBuilder.scheme("https")
        .host("app.orca.jeanbarrossilva.com")
        .path("profile")
        .path("@jeanbarrossilva")
        .path("avatar.jpg")
        .build()
    val actor = MirroredActor.authenticated(delegate.id, delegate.accessToken, avatarURI)
    val jsonObject = Json.encodeToJsonElement(actor).jsonObject
    val decoder = createJsonTreeDecoder(jsonObject)
    assertThat(MirroredActor.serializer().deserialize(decoder)).isEqualTo(actor)
  }
}
