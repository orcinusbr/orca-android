package com.jeanbarrossilva.orca.core.http.instance

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.instance.Instance
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequest
import io.ktor.http.Url

/** An [HttpInstance] with a generic [Authenticator]. **/
internal typealias SomeHttpInstance = HttpInstance<*>

/**
 * [Instance] that performs all of its underlying operations by sending [HttpRequest]s to the API.
 *
 * @param T [Authenticator] to authenticate the user with.
 **/
interface HttpInstance<T : Authenticator> : Instance<T> {
    /** [Url] to which routes will be appended when [HttpRequest]s are sent. **/
    val url: Url

    /** [HttpClient] by which [HttpRequest]s will be sent. **/
    val client: HttpClient
}
