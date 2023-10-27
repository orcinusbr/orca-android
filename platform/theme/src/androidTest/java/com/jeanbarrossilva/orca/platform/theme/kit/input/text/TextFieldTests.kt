package com.jeanbarrossilva.orca.platform.theme.kit.input.text

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.ErrorDispatcher
import org.junit.Rule
import org.junit.Test

internal class TextFieldTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsErrorsWhenTextIsInvalid() {
    val errorDispatcher =
      ErrorDispatcher().apply {
        error("🫵🏽") { true }
        dispatch()
      }
    composeRule.setContent { OrcaTheme { TextField(errorDispatcher = errorDispatcher) } }
    composeRule.onNodeWithTag(TEXT_FIELD_ERRORS_TAG).assertIsDisplayed()
  }
}
