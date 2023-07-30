package com.jeanbarrossilva.mastodonte.core.mastodon.profile.type.editable

import com.jeanbarrossilva.mastodonte.core.mastodon.client.MastodonHttpClient
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndSubmitForm
import com.jeanbarrossilva.mastodonte.core.mastodon.client.authenticateAndSubmitFormWithBinaryData
import com.jeanbarrossilva.mastodonte.core.profile.type.editable.Editor
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parametersOf
import io.ktor.utils.io.streams.asInput
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.name

internal class MastodonEditor : Editor {
    @Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")
    override suspend fun setAvatarURL(avatarURL: URL) {
        val file: Path = TODO()
        val fileAsFile = file.toFile()
        val fileLength = fileAsFile.length()
        val inputProvider = InputProvider(fileLength) { fileAsFile.inputStream().asInput() }
        val contentDisposition = "form-data; name=\"avatar\" filename=\"${file.name}\""
        val headers = Headers.build { append(HttpHeaders.ContentDisposition, contentDisposition) }
        val formData = formData { append("avatar", inputProvider, headers) }
        MastodonHttpClient.authenticateAndSubmitFormWithBinaryData(ROUTE, formData)
    }

    override suspend fun setName(name: String) {
        MastodonHttpClient.authenticateAndSubmitForm(ROUTE, parametersOf("display_name", name))
    }

    override suspend fun setBio(bio: String) {
        MastodonHttpClient.authenticateAndSubmitForm(ROUTE, parametersOf("note", bio))
    }

    companion object {
        private const val ROUTE = "api/v1/accounts/update_credentials"
    }
}
