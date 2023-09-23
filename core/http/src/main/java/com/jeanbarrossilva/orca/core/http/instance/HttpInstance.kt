package com.jeanbarrossilva.orca.core.http.instance

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.instance.Instance
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequest
import io.ktor.http.Url

/** An [HttpInstance] with a generic [Authorizer] and an [Authenticator]. **/
internal typealias SomeHttpInstance = HttpInstance<*, *>

/**
 * [Instance] that performs all of its underlying operations by sending [HttpRequest]s to the API.
 *
 * @param F [Authorizer] with which authorization will be performed.
 * @param S [Authenticator] to authenticate the user with.
 **/
interface HttpInstance<F : Authorizer, S : Authenticator> : Instance<S> {
    /** [Authorizer] by which the user will be authorized. **/
    val authorizer: F

    /** [Url] to which routes will be appended when [HttpRequest]s are sent. **/
    val url: Url

    /** [HttpClient] by which [HttpRequest]s will be sent. **/
    val client: HttpClient
}
