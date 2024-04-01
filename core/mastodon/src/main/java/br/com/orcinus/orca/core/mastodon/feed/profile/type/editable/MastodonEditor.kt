/*
 * Copyright © 2023-2024 Orca
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
import br.com.orcinus.orca.core.mastodon.client.authenticateAndSubmitForm
import br.com.orcinus.orca.core.mastodon.client.authenticateAndSubmitFormWithBinaryData
import br.com.orcinus.orca.core.mastodon.instance.SomeMastodonInstance
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.instanceProvider
import br.com.orcinus.orca.std.image.SomeImageLoader
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.styledstring.StyledString
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parametersOf
import io.ktor.utils.io.streams.asInput
import java.nio.file.Path
import kotlin.io.path.name

/** [Editor] whose actions communicate with the Mastodon API. */
internal class MastodonEditor : Editor {
  @Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")
  override suspend fun setAvatarLoader(avatarLoader: SomeImageLoader) {
    val file: Path = TODO()
    val fileAsFile = file.toFile()
    val fileLength = fileAsFile.length()
    val inputProvider = InputProvider(fileLength) { fileAsFile.inputStream().asInput() }
    val contentDisposition = "form-data; name=\"avatar\" filename=\"${file.name}\""
    val headers = Headers.build { append(HttpHeaders.ContentDisposition, contentDisposition) }
    val formData = formData { append("avatar", inputProvider, headers) }
    (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .client
      .authenticateAndSubmitFormWithBinaryData(ROUTE, formData)
  }

  override suspend fun setName(name: String) {
    (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .client
      .authenticateAndSubmitForm(ROUTE, parametersOf("display_name", name))
  }

  override suspend fun setBio(bio: StyledString) {
    (Injector.from<CoreModule>().instanceProvider().provide() as SomeMastodonInstance)
      .client
      .authenticateAndSubmitForm(ROUTE, parametersOf("note", "$bio"))
  }

  companion object {
    /** Route to which the [HttpRequest]s will be sent for editing A [MastodonEditableProfile]. */
    private const val ROUTE = "api/v1/accounts/update_credentials"
  }
}
