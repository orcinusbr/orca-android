package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post

import com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that represents a thread of [MastodonStatus]es.
 *
 * @param ancestors [MastodonStatus]es to which the referred one is a comment.
 * @param descendants [MastodonStatus]es that have been published as comments to the one this
 *   [MastodonContext] refers to.
 */
@Serializable
internal data class MastodonContext(
  val ancestors: List<MastodonStatus>,
  val descendants: List<MastodonStatus>
)
