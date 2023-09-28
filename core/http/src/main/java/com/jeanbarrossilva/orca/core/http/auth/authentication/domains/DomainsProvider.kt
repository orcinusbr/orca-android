package com.jeanbarrossilva.orca.feature.auth.domains

import com.jeanbarrossilva.orca.core.instance.domain.Domain

/** Provides [Domain]s through [provide]. **/
interface DomainsProvider {
    /** Provides the available Mastodon server [Domain]s. **/
    suspend fun provide(): List<Domain>

    /**
     * Provides the available Mastodon server [Domain]s matching the given [query].
     *
     * @param query Query to provide the [Domain]s with.
     **/
    suspend fun provide(query: String): List<Domain>
}
