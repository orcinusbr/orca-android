package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.LinkHeader
import io.ktor.util.filter
import io.ktor.util.toMap

/**
 * Parametrized URIs present in the Link header, each represented by a [LinkHeader].
 *
 * @see HttpHeaders.Link
 */
internal val Headers.links
  get() =
    filter { key, _ -> key.equals(HttpHeaders.Link, ignoreCase = true) }
      .toMap()
      .values
      .flatten()
      .map {
        LinkHeader(
          uri = it.substringAfter('<').substringBefore('>'),
          rel = it.substringAfter("rel=\"").substringBeforeLast('"')
        )
      }
