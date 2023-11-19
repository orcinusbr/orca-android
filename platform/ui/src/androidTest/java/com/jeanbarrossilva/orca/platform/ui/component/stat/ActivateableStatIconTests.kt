package com.jeanbarrossilva.orca.platform.ui.component.stat

import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.test.onActivateableStatIcon
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class ActivateableStatIconTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isUnselectedWhenInactive() {
    composeRule.setContent { AutosTheme { TestActivateableStatIcon(isActive = false) } }
    composeRule.onActivateableStatIcon().assertIsNotSelected()
  }

  @Test
  fun isSelectedWhenActive() {
    composeRule.setContent { AutosTheme { TestActivateableStatIcon(isActive = true) } }
    composeRule.onActivateableStatIcon().assertIsSelected()
  }

  @Test
  fun receivesInteractionWhenInteractive() {
    var hasBeenInteractedWith = false
    composeRule.setContent {
      AutosTheme {
        TestActivateableStatIcon(
          interactiveness =
            ActivateableStatIconInteractiveness.Interactive { hasBeenInteractedWith = true }
        )
      }
    }
    composeRule.onActivateableStatIcon().performClick()
    assertTrue(hasBeenInteractedWith)
  }
}
