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

package br.com.orcinus.orca.core.feed.profile.post.content

import br.com.orcinus.orca.core.InternalCoreApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/** Mutes and retrieves terms that have been muted. */
abstract class TermMuter @InternalCoreApi constructor() {
  /** [MutableSharedFlow] to which the muted terms are emitted. */
  private val termsMutableFlow = MutableSharedFlow<HashSet<String>>(replay = 1)

  /** Terms that are currently muted. */
  private val currentTerms
    get() = termsMutableFlow.replayCache.singleOrNull() ?: initialTerms

  /** Terms which are muted by default. */
  protected abstract val initialTerms: HashSet<String>

  /** [Flow] to which the muted terms are emitted. */
  val termsFlow = termsMutableFlow.onStart { emit(initialTerms) }.map(HashSet<String>::toList)

  /**
   * Whether the [content] contains muted terms.
   *
   * @param content [Content] whose terms will be verified.
   */
  fun isMuted(content: Content): Boolean {
    return currentTerms.any { it in content.text }
  }

  /**
   * Mutes the given [term].
   *
   * @param term Term to be muted.
   */
  suspend fun mute(term: String) {
    onMuting(term)
    termsMutableFlow.emit(HashSet(currentTerms + term))
  }

  /**
   * Unmutes the given [term].
   *
   * @param term Term to be unmuted.
   */
  suspend fun unmute(term: String) {
    onUnmuting(term)
    termsMutableFlow.emit(HashSet(currentTerms - term))
  }

  /**
   * Callback called when the given [term] is requested to be muted.
   *
   * @param term Term to be muted.
   */
  protected abstract suspend fun onMuting(term: String)

  /**
   * Callback called when the given [term] is requested to be unmuted.
   *
   * @param term Term to be unmuted.
   */
  protected abstract suspend fun onUnmuting(term: String)
}
