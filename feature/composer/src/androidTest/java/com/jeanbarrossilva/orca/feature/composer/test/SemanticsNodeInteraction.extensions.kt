package com.jeanbarrossilva.orca.feature.composer.test

import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.text.AnnotatedString

/**
 * Asserts that the [SemanticsNode] has exactly the given text [values].
 *
 * @param values Texts that the [SemanticsNode] is expected to have.
 */
internal fun SemanticsNodeInteraction.assertTextEquals(vararg values: AnnotatedString) {
  assert(hasTextExactly(*values))
}
