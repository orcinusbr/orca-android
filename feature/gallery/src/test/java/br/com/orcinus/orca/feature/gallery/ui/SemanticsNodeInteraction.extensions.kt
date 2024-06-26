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

package br.com.orcinus.orca.feature.gallery.ui

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction

/**
 * Returns whether the [matcher] matches the [SemanticsNode] fetched from this
 * [SemanticsNodeInteraction].
 *
 * @param matcher [SemanticsMatcher] to match the [SemanticsNode] against.
 * @see SemanticsMatcher.matches
 * @see SemanticsNodeInteraction.fetchSemanticsNode
 */
internal operator fun SemanticsNodeInteraction.get(matcher: SemanticsMatcher): Boolean {
  val node = fetchSemanticsNode()
  return matcher.matches(node)
}
