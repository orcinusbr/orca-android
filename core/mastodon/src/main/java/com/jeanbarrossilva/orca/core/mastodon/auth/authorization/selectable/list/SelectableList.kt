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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable.list

import androidx.annotation.Discouraged
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable.Selectable

/**
 * [List] capable of holding [Selectable]s.
 *
 * @param elements [Selectable]s contained in this [SelectableList].
 * @throws IllegalStateException If none or more than one [Selectable] is selected.
 */
@JvmInline
internal value class SelectableList<T>
@Discouraged("Use `selectableListOf` or `emptySelectableList` instead.")
internal constructor(private val elements: List<Selectable<T>>) : List<Selectable<T>> by elements {
  /** Value that's currently selected. */
  val selected
    get() = single(Selectable<T>::isSelected).value

  /** [IllegalStateException] thrown if more than one [Selectable] are selected. */
  class MultipleSelectionException : IllegalStateException("Only one element can be selected.")

  init {
    ensureSelectionConstraint()
  }

  /**
   * Gets the [Selectable] whose [value][Selectable.value] equals to the given one.
   *
   * @param value [Value][Selectable.value] of the [Selectable] to be obtained.
   */
  @Suppress("KDocUnresolvedReference")
  operator fun get(value: T): Selectable<T>? {
    return find { selectable -> selectable.value == value }
  }

  /**
   * Selects the [Selectable] whose [value][Selectable.value] equals to the given one.
   *
   * @param value [Value][Selectable.value] of the [Selectable] to be selected.
   */
  @Suppress("KDocUnresolvedReference")
  fun select(value: T): SelectableList<T> {
    return map(Selectable<T>::value).select(value)
  }

  /**
   * Ensures that selection is constrained to zero or one [Selectable]s.
   *
   * @throws MultipleSelectionException If more than one [Selectable] are selected.
   */
  @Throws(MultipleSelectionException::class)
  private fun ensureSelectionConstraint() {
    val hasMultipleSelections = elements.count(Selectable<T>::isSelected) > 1
    if (hasMultipleSelections) {
      throw MultipleSelectionException()
    }
  }
}
