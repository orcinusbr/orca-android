package com.jeanbarrossilva.orca.platform.theme.kit.input.text

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.ErrorDispatcher
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.error.buildErrorDispatcher
import com.jeanbarrossilva.orca.platform.theme.test.kit.input.text.onTextFieldErrors
import org.junit.Rule
import org.junit.Test

internal class TextFieldTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun showsErrorsWhenTextIsInvalid() {
    val errorDispatcher =
      buildErrorDispatcher { errorAlways("🫵🏽") }.apply(ErrorDispatcher::dispatch)
    composeRule.setContent { OrcaTheme { TextField(errorDispatcher = errorDispatcher) } }
    composeRule.onTextFieldErrors().assertIsDisplayed()
  }
}
