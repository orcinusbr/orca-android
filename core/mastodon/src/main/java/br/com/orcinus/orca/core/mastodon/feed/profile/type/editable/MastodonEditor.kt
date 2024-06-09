/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.type.editable

import br.com.orcinus.orca.core.feed.profile.type.editable.Editor
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.authenticated
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.markdown.Markdown
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.streams.asInput
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.name

/** [Editor] whose actions communicate with the Mastodon API. */
internal class MastodonEditor : Editor {
  /** [URI] to which requests for editing a [MastodonEditableProfile] are to be sent. */
  private val editRoute: HostedURLBuilder.() -> URI = {
    path("api").path("v1").path("accounts").path("update_credentials").build()
  }

  @Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")
  override suspend fun setAvatarLoader(avatarLoader: SomeImageLoader) {
    val file: Path = TODO()
    val fileAsFile = file.toFile()
    val fileLength = fileAsFile.length()
    val inputProvider = InputProvider(fileLength) { fileAsFile.inputStream().asInput() }
    val contentDisposition = "form-data; name=\"avatar\" filename=\"${file.name}\""
    val headers = Headers.build { append(HttpHeaders.ContentDisposition, contentDisposition) }
    val form = formData { append("avatar", inputProvider, headers) }
    (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .requester
      .authenticated()
      .post(editRoute, form)
  }

  override suspend fun setName(name: String) {
    (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .requester
      .authenticated()
      .post(editRoute) { parameters { append("display_name", name) } }
  }

  override suspend fun setBio(bio: Markdown) {
    (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .requester
      .authenticated()
      .post(editRoute) { parameters { append("note", "$bio") } }
  }
}
