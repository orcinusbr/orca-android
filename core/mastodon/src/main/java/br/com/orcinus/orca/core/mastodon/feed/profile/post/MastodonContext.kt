/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.feed.profile.post

import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus
import kotlinx.serialization.Serializable

/**
 * Structure returned by the API that represents a thread of [MastodonStatus]es.
 *
 * @property descendants [MastodonStatus]es that have been published as comments to the one this
 *   [MastodonContext] refers to.
 */
@Serializable internal data class MastodonContext(val descendants: List<MastodonStatus>)
