/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.composite.searchable

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField

/** [SearchableScope] implementation provided to the content of a [Searchable]. */
class SearchableScope internal constructor() {
  /** [LazyListScope] of the lazy list of decorations. */
  private var decorationLazyListScope: LazyListScope? = null

  /** Content to be shown by default and that can be replaced. */
  internal var content by mutableStateOf<(@Composable () -> Unit)?>(null)
    private set

  /** Whether the [SearchTextField] is currently being shown. */
  var isSearching by mutableStateOf(false)
    private set

  /**
   * Sets the content to be replaced by the [SearchTextField].
   *
   * @param content Content to be shown by default and that can be replaced.
   */
  fun content(content: @Composable () -> Unit) {
    this.content = content
  }

  /** Shows the [SearchTextField]. */
  fun show() {
    isSearching = true
  }

  /**
   * Provides a [LazyListScope] to which decorations can be added.
   *
   * @param decorations Adds contents to be displayed alongside the [SearchTextField].
   */
  fun decorations(decorations: LazyListScope.() -> Unit) {
    decorationLazyListScope?.decorations()
  }

  /** Dismisses the [SearchTextField]. */
  fun dismiss() {
    isSearching = false
  }

  /**
   * Defines the [LazyListScope] to which decorations are added.
   *
   * @param decorationLazyListScope [LazyListScope] of the lazy list of decorations.
   * @see decorations
   */
  internal fun setDecorationLazyListScope(decorationLazyListScope: LazyListScope) {
    this.decorationLazyListScope = decorationLazyListScope
  }
}
