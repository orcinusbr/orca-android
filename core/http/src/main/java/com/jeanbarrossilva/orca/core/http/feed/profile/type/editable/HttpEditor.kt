package com.jeanbarrossilva.orca.core.http.feed.profile.type.editable

import com.jeanbarrossilva.orca.core.feed.profile.type.editable.Editor
import com.jeanbarrossilva.orca.core.http.HttpBridge
import com.jeanbarrossilva.orca.core.http.client.authenticateAndSubmitForm
import com.jeanbarrossilva.orca.core.http.client.authenticateAndSubmitFormWithBinaryData
import com.jeanbarrossilva.orca.std.styledstring.StyledString
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parametersOf
import io.ktor.utils.io.streams.asInput
import java.net.URL
import java.nio.file.Path
import kotlin.io.path.name

/** [Editor] whose actions send [HttpRequest]s to the API. **/
internal class HttpEditor : Editor {
    @Suppress("UNREACHABLE_CODE", "UNUSED_VARIABLE")
    override suspend fun setAvatarURL(avatarURL: URL) {
        val file: Path = TODO()
        val fileAsFile = file.toFile()
        val fileLength = fileAsFile.length()
        val inputProvider = InputProvider(fileLength) { fileAsFile.inputStream().asInput() }
        val contentDisposition = "form-data; name=\"avatar\" filename=\"${file.name}\""
        val headers = Headers.build { append(HttpHeaders.ContentDisposition, contentDisposition) }
        val formData = formData { append("avatar", inputProvider, headers) }
        HttpBridge.instance.client.authenticateAndSubmitFormWithBinaryData(ROUTE, formData)
    }

    override suspend fun setName(name: String) {
        HttpBridge.instance.client.authenticateAndSubmitForm(
            ROUTE,
            parametersOf("display_name", name)
        )
    }

    override suspend fun setBio(bio: StyledString) {
        HttpBridge.instance.client.authenticateAndSubmitForm(ROUTE, parametersOf("note", "$bio"))
    }

    companion object {
        /** Route to which the [HttpRequest]s will be sent for editing an [HttpEditableProfile]. **/
        private const val ROUTE = "api/v1/accounts/update_credentials"
    }
}
