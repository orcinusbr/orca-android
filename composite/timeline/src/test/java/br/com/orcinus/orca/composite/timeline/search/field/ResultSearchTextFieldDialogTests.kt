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

package br.com.orcinus.orca.composite.timeline.search.field

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.Espresso.pressBack
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.composite.timeline.test.search.field.isResultSearchTextField
import br.com.orcinus.orca.composite.timeline.test.search.field.onDismissButton
import kotlin.test.Test
import kotlinx.coroutines.launch
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ResultSearchTextFieldDialogTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun shows() {
    OwnedResultSearchTextFieldDialog(composeRule.activity)
      .apply(OwnedResultSearchTextFieldDialog::show)
      .also { composeRule.onRoot().onChild().assert(isResultSearchTextField()) }
      .dismiss()
  }

  @Test
  fun dismisses() {
    OwnedResultSearchTextFieldDialog(composeRule.activity)
      .apply(OwnedResultSearchTextFieldDialog::show)
      .dismiss()
    composeRule.onRoot().assertDoesNotExist()
  }

  @Test
  fun dismissesWhenBackPressing() {
    val dialog =
      OwnedResultSearchTextFieldDialog(composeRule.activity)
        .apply(OwnedResultSearchTextFieldDialog::show)
    pressBack()
    composeRule.onRoot().assertDoesNotExist()
    dialog.dismiss()
  }

  @Test
  fun dismissesWhenClickingDismissalButton() {
    OwnedResultSearchTextFieldDialog(composeRule.activity).show()
    composeRule.onDismissButton().performClick()
    composeRule.onRoot().assertDoesNotExist()
  }

  @Test
  fun listensToDismissalOnce() {
    var hasNotified = false
    OwnedResultSearchTextFieldDialog(composeRule.activity)
      .apply {
        show()
        doOnDidDismiss { hasNotified = true }
      }
      .dismiss()
    composeRule.activity.lifecycleScope.launch { assertThat(hasNotified).isTrue() }
  }

  @Test
  fun listensToDismissalMultipleTimes() {
    var notificationCount = 0
    OwnedResultSearchTextFieldDialog(composeRule.activity)
      .apply {
        show()
        repeat(64) { doOnDidDismiss { notificationCount++ } }
      }
      .dismiss()
    composeRule.activity.lifecycleScope.launch { assertThat(notificationCount).isEqualTo(64) }
  }
}
