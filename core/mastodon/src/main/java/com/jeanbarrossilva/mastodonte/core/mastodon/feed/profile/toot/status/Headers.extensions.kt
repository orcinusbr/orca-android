package com.jeanbarrossilva.mastodonte.core.mastodon.feed.profile.toot.status

import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.LinkHeader

/**
 * Parametrized URIs present in the Link header, each represented by a [LinkHeader].
 *
 * **NOTE**: Only suited for rel-parametrized URIs.
 *
 * @see HttpHeaders.Link
 * @see LinkHeader.Parameters.Rel
 **/
internal val Headers.links
    get() = get(HttpHeaders.Link)?.split(',')?.map { it.split(';') }?.map {
        LinkHeader(uri = it.first().removePrefix("<").removeSuffix(">"), it[1].removePrefix("rel="))
    }
