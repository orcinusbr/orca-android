package com.jeanbarrossilva.orca.core.http.feed.profile.toot

import com.jeanbarrossilva.orca.core.http.feed.profile.toot.status.HttpStatus
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that represents a thread of [HttpStatus]es.
 *
 * @param ancestors [HttpStatus]es to which the referred one is a comment.
 * @param descendants [HttpStatus]es that have been published as comments to the one this
 *   [HttpContext] refers to.
 */
@Serializable
internal data class HttpContext(val ancestors: List<HttpStatus>, val descendants: List<HttpStatus>)
