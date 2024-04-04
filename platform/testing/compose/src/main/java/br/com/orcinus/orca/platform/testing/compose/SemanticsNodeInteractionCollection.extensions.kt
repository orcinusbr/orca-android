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

package br.com.orcinus.orca.platform.testing.compose

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionCollection

/**
 * Whether this [AssertionError] is the result of asserting the existence of a given
 * [SemanticsNode], which denotes that there isn't one at the index at which it was tried to be
 * accessed.
 *
 * Since there is no public API in Jetpack Compose (as per 1.6.0-rc01) for retrieving all of the
 * [SemanticsNodeInteraction]s within a [SemanticsNodeInteractionCollection], it can be used to
 * indicate that its end has been reached.
 *
 * @see END
 */
private val AssertionError.isBecauseIndexIsOutOfBounds
  get() = message?.startsWith("Failed: assertExists.\nCan't retrieve node at index '") ?: false

/**
 * Indicates that the end of a [SemanticsNodeInteractionCollection] has been reached due to the
 * absence of a [SemanticsNodeInteraction] at a given index.
 *
 * @see java.lang.AssertionError.isBecauseIndexIsOutOfBounds
 */
private object END

/**
 * Performs the given [action] on each of the [SemanticsNodeInteraction]s.
 *
 * @param action Operation to be run on each [SemanticsNode]'s [SemanticsNodeInteraction].
 */
fun SemanticsNodeInteractionCollection.onEach(
  action: SemanticsNodeInteraction.(index: Int) -> Unit
): SemanticsNodeInteractionCollection {
  var index = 0
  var current: Any? = null
  while (current !== END) {
    current =
      try {
        get(index).also(SemanticsNodeInteraction::assertExists)
      } catch (error: AssertionError) {
        if (error.isBecauseIndexIsOutOfBounds) {
          END
        } else {
          throw error
        }
      }
    (current as? SemanticsNodeInteraction)?.action(index++)
  }
  return this
}
