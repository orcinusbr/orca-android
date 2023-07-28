package com.jeanbarrossilva.mastodonte.core.mastodon

import io.ktor.client.request.HttpRequest
import io.ktor.http.Url

/** Mastodon-API-related keys and configurations. **/
internal object Mastodon {
    /** Client ID (or key) that, unlinke the [clientSecret], is be publicly visible. **/
    @Suppress("SpellCheckingInspection")
    const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

    /**
     * Client secret that, unlike the [CLIENT_ID], is private and is mainly used to obtain an access
     * token.
     **/
    @Suppress("MayBeConstant", "RedundantSuppression")
    val clientSecret = BuildConfig.mastodonclientSecret

    /** [Url] to which [HttpRequest] routes will be concatenated. **/
    val baseUrl = Url("https://mastodon.social")

    /** Denotes the site through which scopes are being provided. **/
    enum class ScopeProvisionSite {
        /** Denotes that scopes are being provided through query parameters. **/
        QUERY_PARAMETER {
            override val separator = '+'
        },

        /** Denotes that scopes are being provided through a form. **/
        FORM {
            override val separator = ' '
        };

        /** [Char] used to separate different scopes. **/
        abstract val separator: Char
    }

    /**
     * Gets the scopes required by the application for it to function properly.
     *
     * @param provisionSite [ScopeProvisionSite] whose [separator][ScopeProvisionSite.separator]
     * will be used to format the resulting [String].
     * @return [String] formatted accordingly to the [provisionSite], containing all the required
     * scopes.
     **/
    fun getScopes(provisionSite: ScopeProvisionSite): String {
        return arrayOf("read", "write", "follow").joinToString("${provisionSite.separator}")
    }
}
