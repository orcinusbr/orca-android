package com.jeanbarrossilva.orca.feature.composer.test

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.text.AnnotatedString
import com.jeanbarrossilva.orca.feature.composer.ui.COMPOSER_TOOLBAR_BOLD_FORMAT
import com.jeanbarrossilva.orca.feature.composer.ui.COMPOSER_TOOLBAR_ITALIC_FORMAT
import com.jeanbarrossilva.orca.feature.composer.ui.COMPOSER_TOOLBAR_UNDERLINE_FORMAT
import com.jeanbarrossilva.orca.feature.composer.ui.Toolbar

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
