/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.feed.profile.post.content

import com.jeanbarrossilva.orca.std.buildable.Buildable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

/**
 * Mutes and retrieves terms that have been muted.
 *
 * An instance of this class can be created via its factory method.
 */
@Buildable
abstract class TermMuter internal constructor() {
  /**
   * Whether the [content] contains muted terms.
   *
   * @param content [Content] whose terms will be verified.
   */
  suspend fun isMuted(content: Content): Boolean {
    val terms = getTerms().first()
    return terms.any { it in content.text }
  }

  /** Gets the [Flow] to which the muted terms are emitted. */
  open fun getTerms(): Flow<List<String>> {
    return flowOf(emptyList())
  }

  /**
   * Mutes the given [term].
   *
   * @param term Term to be muted.
   */
  open suspend fun mute(term: String) {}

  /**
   * Unmutes the given [term].
   *
   * @param term Term to be unmuted.
   */
  open suspend fun unmute(term: String) {}
}
