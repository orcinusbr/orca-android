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

package br.com.orcinus.orca.composite.timeline.test.search.field

import androidx.compose.material3.Card
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import br.com.orcinus.orca.composite.timeline.search.field.DismissButtonTag
import br.com.orcinus.orca.composite.timeline.search.field.ResultCardTag
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextField
import br.com.orcinus.orca.composite.timeline.search.field.ResultSearchTextFieldTag

/**
 * [SemanticsMatcher] that matches a [ResultSearchTextField]'s result [Card].
 *
 * @see isResultSearchTextField
 */
fun isResultCard(): SemanticsMatcher {
  return SemanticsMatcher("is result card") {
    it.config.getOrNull(SemanticsProperties.TestTag) == ResultCardTag
  }
}

/** [SemanticsMatcher] that matches a [ResultSearchTextField]. */
fun isResultSearchTextField() =
  SemanticsMatcher("is result search text field") {
    it.config.getOrNull(SemanticsProperties.TestTag) == ResultSearchTextFieldTag
  }

/**
 * [SemanticsMatcher] that matches a [ResultSearchTextField]'s "dismiss" button.
 *
 * @see isResultSearchTextField
 */
internal fun isDismissButton(): SemanticsMatcher {
  return SemanticsMatcher("is dismiss button") {
    it.config.getOrNull(SemanticsProperties.TestTag) == DismissButtonTag
  }
}
