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
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import assertk.all
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import io.mockk.mockk
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class OwnedResultSearchTextFieldDialogTests {
  private inline val activity
    get() = composeRule.activity

  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun doesNotDeconfigureActivityViewTreeOwnershipWhenDismissedWhileNotShown() {
    val originalViewTreeLifecycleOwner = mockk<LifecycleOwner>()
    val originalViewTreeViewModelStoreOwner = mockk<ViewModelStoreOwner>()
    val originalViewTreeSavedStateRegistryOwner = mockk<SavedStateRegistryOwner>()
    activity.window?.decorView?.apply {
      setViewTreeLifecycleOwner(originalViewTreeLifecycleOwner)
      setViewTreeViewModelStoreOwner(originalViewTreeViewModelStoreOwner)
      setViewTreeSavedStateRegistryOwner(originalViewTreeSavedStateRegistryOwner)
    }
    OwnedResultSearchTextFieldDialog(activity).dismiss()
    assertThat(activity).prop(ComponentActivity::getWindow).prop(Window::getDecorView).all {
      prop(View::findViewTreeLifecycleOwner).isSameInstanceAs(originalViewTreeLifecycleOwner)
      prop(View::findViewTreeViewModelStoreOwner)
        .isSameInstanceAs(originalViewTreeViewModelStoreOwner)
      prop(View::findViewTreeSavedStateRegistryOwner)
        .isSameInstanceAs(originalViewTreeSavedStateRegistryOwner)
    }
  }

  @Test
  fun deconfiguresActivityViewTreeOwnershipWhenDismissed() {
    val originalViewTreeLifecycleOwner = mockk<LifecycleOwner>()
    val originalViewTreeViewModelStoreOwner = mockk<ViewModelStoreOwner>()
    val originalViewTreeSavedStateRegistryOwner = mockk<SavedStateRegistryOwner>()
    activity.window?.decorView?.apply {
      setViewTreeLifecycleOwner(originalViewTreeLifecycleOwner)
      setViewTreeViewModelStoreOwner(originalViewTreeViewModelStoreOwner)
      setViewTreeSavedStateRegistryOwner(originalViewTreeSavedStateRegistryOwner)
    }
    OwnedResultSearchTextFieldDialog(activity)
      .apply(OwnedResultSearchTextFieldDialog::show)
      .dismiss()
    assertThat(activity).prop(ComponentActivity::getWindow).prop(Window::getDecorView).all {
      prop(View::findViewTreeLifecycleOwner).isSameInstanceAs(originalViewTreeLifecycleOwner)
      prop(View::findViewTreeViewModelStoreOwner)
        .isSameInstanceAs(originalViewTreeViewModelStoreOwner)
      prop(View::findViewTreeSavedStateRegistryOwner)
        .isSameInstanceAs(originalViewTreeSavedStateRegistryOwner)
    }
  }

  @Test
  fun initializesActivityViewTreeOwnersOnlyOnceWhenShown() {
    OwnedResultSearchTextFieldDialog(activity).apply(OwnedResultSearchTextFieldDialog::show).also {
      assertThat(activity).prop(ComponentActivity::getWindow).prop(Window::getDecorView).all {
        prop(View::findViewTreeLifecycleOwner).isSameInstanceAs(composeRule.activity)
        prop(View::findViewTreeViewModelStoreOwner).isSameInstanceAs(composeRule.activity)
        prop(View::findViewTreeSavedStateRegistryOwner).isSameInstanceAs(composeRule.activity)
      }
    }
  }

  @Test
  fun configuresActivityViewTreeOwnershipWhenShown() {
    val dialog = OwnedResultSearchTextFieldDialog(activity)
    activity.setContent { dialog.Content() }
    assertThat(activity).prop(ComponentActivity::getWindow).prop(Window::getDecorView).all {
      prop(View::findViewTreeLifecycleOwner).isNotNull()
      prop(View::findViewTreeViewModelStoreOwner).isNotNull()
      prop(View::findViewTreeSavedStateRegistryOwner).isNotNull()
    }
  }
}
