package com.jeanbarrossilva.orca.core.http.instance

import com.jeanbarrossilva.orca.core.auth.Authenticator
import com.jeanbarrossilva.orca.core.auth.Authorizer
import com.jeanbarrossilva.orca.core.http.client.CoreHttpClient
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequest
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.set
import io.ktor.http.takeFrom

/** An [HttpInstance] with a generic [Authorizer] and an [Authenticator]. */
internal typealias SomeHttpInstance = HttpInstance<*, *>

/**
 * [Instance] that performs all of its underlying operations by sending [HttpRequest]s to the API.
 *
 * @param F [Authorizer] with which authorization will be performed.
 * @param S [Authenticator] to authenticate the user with.
 */
abstract class HttpInstance<F : Authorizer, S : Authenticator>(final override val domain: Domain) :
    Instance<S>() {
    /** [Authorizer] by which the user will be authorized. **/
    internal abstract val authorizer: F

    /** [Url] to which routes will be appended when [HttpRequest]s are sent. */
    internal val url = URLBuilder().apply { set(scheme = "https", host = "$domain") }.build()

    /** [HttpClient] by which [HttpRequest]s will be sent. */
    internal open val client = CoreHttpClient {
        defaultRequest {
            url.takeFrom(this@HttpInstance.url)
        }
    }
}
