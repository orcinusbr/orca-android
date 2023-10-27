package com.jeanbarrossilva.orca.platform.theme.test.kit.input.text

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.TEXT_FIELD_ERRORS_TAG
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.TextField

/** [SemanticsNodeInteraction] of a [TextField]'s errors. */
fun ComposeTestRule.onTextFieldErrors(): SemanticsNodeInteraction {
  return onNodeWithTag(TEXT_FIELD_ERRORS_TAG)
}
