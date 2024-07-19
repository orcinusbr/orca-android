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

package br.com.orcinus.orca.composite.timeline.search.content

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.com.orcinus.orca.composite.timeline.search.Searchable
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField

/**
 * Scope of a [Searchable] in which the [SearchTextField] can be either shown or dismissed.
 *
 * @property isReplaceableComposedState [State] whose [Boolean] determines whether the content to be
 *   replaced by the [SearchTextField] is currently composed.
 */
class SearchableReplacementScope
internal constructor(private val isReplaceableComposedState: State<Boolean>) {
  /** Whether the [ResultSearchTextField] is currently being shown. */
  internal var isSearching by mutableStateOf(false)
    private set

  /** Shows the [SearchTextField]. */
  fun show() {
    isSearching = isReplaceableComposedState.value
  }

  /** Dismisses the [ResultSearchTextField]. */
  internal fun dismiss() {
    isSearching = false
  }
}
