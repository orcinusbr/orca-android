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

package br.com.orcinus.orca.feature.composer.test

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.text.AnnotatedString
import br.com.orcinus.orca.feature.composer.ui.COMPOSER_TOOLBAR_BOLD_FORMAT
import br.com.orcinus.orca.feature.composer.ui.COMPOSER_TOOLBAR_ITALIC_FORMAT
import br.com.orcinus.orca.feature.composer.ui.COMPOSER_TOOLBAR_UNDERLINE_FORMAT
import br.com.orcinus.orca.feature.composer.ui.Toolbar

/**
 * [SemanticsMatcher] that indicate whether the [SemanticsNode] has all text [values], editable or
 * non-editable.
 *
 * @param values [AnnotatedString]s that the [SemanticsNode] is expected to have.
 */
internal fun hasTextExactly(vararg values: AnnotatedString): SemanticsMatcher {
  val expected = values.toList()
  return SemanticsMatcher(
    "${SemanticsProperties.Text.name} + ${SemanticsProperties.EditableText.name} = $expected"
  ) { node ->
    with(node.config) {
      getOrNull(SemanticsProperties.Text)
        .orEmpty()
        .plus(getOrNull(SemanticsProperties.EditableText))
        .filterNotNull()
        .let { texts -> texts.containsAll(expected) && expected.containsAll(texts) }
    }
  }
}

/**
 * [SemanticsMatcher] that indicates whether the [SemanticsNode] is of a [Toolbar]'s bold format.
 */
internal fun isBoldFormat(): SemanticsMatcher {
  return SemanticsMatcher("is bold format") {
    it.config.getOrNull(SemanticsProperties.TestTag) == COMPOSER_TOOLBAR_BOLD_FORMAT
  }
}

/**
 * [SemanticsMatcher] that indicates whether the [SemanticsNode] is of a [Toolbar]'s italic format.
 */
internal fun isItalicFormat(): SemanticsMatcher {
  return SemanticsMatcher("is italic format") {
    it.config.getOrNull(SemanticsProperties.TestTag) == COMPOSER_TOOLBAR_ITALIC_FORMAT
  }
}

/**
 * [SemanticsMatcher] that indicates whether the [SemanticsNode] if of a [Toolbar]'s underline
 * format.
 */
internal fun isUnderlineFormat(): SemanticsMatcher {
  return SemanticsMatcher("is underline format") {
    it.config.getOrNull(SemanticsProperties.TestTag) == COMPOSER_TOOLBAR_UNDERLINE_FORMAT
  }
}
