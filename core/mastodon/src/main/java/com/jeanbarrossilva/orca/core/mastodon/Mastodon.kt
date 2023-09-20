package com.jeanbarrossilva.orca.core.mastodon

import io.ktor.client.request.HttpRequest
import io.ktor.http.Url

/** Mastodon-API-related keys and configurations. **/
internal object Mastodon {
    /** Client ID (or key) that, unlinke the [clientSecret], is be publicly visible. **/
    @Suppress("SpellCheckingInspection")
    const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

    /** Mastodon's official instance. **/
    const val INSTANCE = "mastodon.social"

    /**
     * Client secret that, unlike the [CLIENT_ID], is private and is mainly used to obtain an access
     * token.
     **/
    @Suppress("MayBeConstant", "RedundantSuppression")
    val clientSecret = BuildConfig.mastodonclientSecret

    /** [Url] to which [HttpRequest] routes will be concatenated. **/
    val baseUrl = Url("https://mastodon.social")

    /** Scopes required by the application for it to function properly. **/
    val scopes = arrayOf("read", "write", "follow").joinToString(separator = " ")
}
