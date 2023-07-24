package com.jeanbarrossilva.mastodonte.core.mastodon.toot.status

import io.ktor.http.Headers
import io.ktor.http.LinkHeader

/**
 * [LinkHeader]s among the given ones.
 *
 * **NOTE**: Only suited for [LinkHeader.Parameters.Rel]-parametrized [LinkHeader]s.
 **/
internal val Headers.links
    get() = get("Link")?.split(',')?.map { it.split(';') }?.map {
        LinkHeader(uri = it.first().removePrefix("<").removeSuffix(">"), it[1].removePrefix("rel="))
    }
