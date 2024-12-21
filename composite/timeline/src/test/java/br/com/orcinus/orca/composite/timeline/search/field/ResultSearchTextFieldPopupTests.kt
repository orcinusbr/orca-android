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

import android.view.View
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso.pressBack
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isTrue
import assertk.assertions.prop
import br.com.orcinus.orca.composite.timeline.test.search.field.onDismissButton
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onSearchTextField
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ResultSearchTextFieldPopupTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun doesNotDeconfigureActivityViewTreeOwnershipWhenDismissedWhileNotShown() {
    val originalOwnedTreeView = View(composeRule.activity)
    composeRule.activity.setContentView(originalOwnedTreeView)
    val originalViewTreeOwner = ViewTreeOwner.of(originalOwnedTreeView)
    OwnedResultSearchTextFieldPopup(composeRule.activity).dismiss()
    assertThat(ViewTreeOwner)
      .prop("from") { it.from(composeRule.activity) }
      .isEqualTo(originalViewTreeOwner)
  }

  @Test
  fun shows() =
    OwnedResultSearchTextFieldPopup(composeRule.activity)
      .apply(OwnedResultSearchTextFieldPopup::show)
      .also { composeRule.onSearchTextField().assertIsDisplayed() }
      .dismiss()

  @Test
  fun throwsOnSimultaneousContents() {
    val popup = OwnedResultSearchTextFieldPopup(composeRule.activity)
    assertFailure { composeRule.setContent { repeat(2) { popup.Content() } } }
      .isInstanceOf<IllegalStateException>()
    popup.dismiss()
  }

  @Test
  fun dismisses() {
    OwnedResultSearchTextFieldPopup(composeRule.activity)
      .apply(OwnedResultSearchTextFieldPopup::show)
      .dismiss()
    composeRule.onRoot().assertDoesNotExist()
  }

  @Test
  fun dismissesWhenBackPressing() {
    val popup =
      OwnedResultSearchTextFieldPopup(composeRule.activity)
        .apply(OwnedResultSearchTextFieldPopup::show)
    pressBack()
    composeRule.onRoot().assertDoesNotExist()
    popup.dismiss()
  }

  @Test
  fun dismissesWhenClickingDismissalButton() {
    OwnedResultSearchTextFieldPopup(composeRule.activity).show()
    composeRule.onDismissButton().performClick()
    composeRule.onRoot().assertDoesNotExist()
  }

  @Test
  fun deconfiguresActivityViewTreeOwnershipWhenDismissed() {
    val originalOwnedTreeView = View(composeRule.activity)
    composeRule.activity.setContentView(originalOwnedTreeView)
    val originalViewTreeOwner = ViewTreeOwner.of(originalOwnedTreeView)
    OwnedResultSearchTextFieldPopup(composeRule.activity)
      .apply(OwnedResultSearchTextFieldPopup::show)
      .dismiss()
    assertThat(ViewTreeOwner)
      .prop("from") { it.from(composeRule.activity) }
      .isEqualTo(originalViewTreeOwner)
  }

  @Test
  fun listensToDismissalOnce() {
    var hasNotified = false
    OwnedResultSearchTextFieldPopup(composeRule.activity)
      .apply {
        show()
        doOnDidDismiss { hasNotified = true }
      }
      .dismiss()
    composeRule.waitForIdle()
    assertThat(hasNotified).isTrue()
  }

  @Test
  fun listensToDismissalMultipleTimes() {
    var notificationCount = 0
    OwnedResultSearchTextFieldPopup(composeRule.activity)
      .apply {
        show()
        repeat(64) { doOnDidDismiss { notificationCount++ } }
      }
      .dismiss()
    composeRule.waitForIdle()
    assertThat(notificationCount).isEqualTo(64)
  }
}
