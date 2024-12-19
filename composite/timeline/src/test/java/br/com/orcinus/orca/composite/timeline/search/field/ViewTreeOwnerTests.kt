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
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isSameInstanceAs
import assertk.assertions.prop
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ViewTreeOwnerTests {
  @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

  @Test
  fun instantiatesFromActivity() =
    assertThat(ViewTreeOwner)
      .transform("from") { it.from(composeRule.activity) }
      .all {
        prop(ViewTreeOwner::lifecycle).isSameInstanceAs(composeRule.activity.lifecycle)
        prop(ViewTreeOwner::viewModelStore).isSameInstanceAs(composeRule.activity.viewModelStore)
        prop(ViewTreeOwner::savedStateRegistry)
          .isSameInstanceAs(composeRule.activity.savedStateRegistry)
      }

  @Test
  fun instantiatesFromView() {
    val view = View(composeRule.activity)
    composeRule.activity.setContentView(view)
    assertThat(ViewTreeOwner)
      .transform("of") { it.of(view) }
      .isNotNull()
      .all {
        prop(ViewTreeOwner::lifecycle)
          .isSameInstanceAs(view.findViewTreeLifecycleOwner()?.lifecycle)
        prop(ViewTreeOwner::viewModelStore)
          .isSameInstanceAs(view.findViewTreeViewModelStoreOwner()?.viewModelStore)
        prop(ViewTreeOwner::savedStateRegistry)
          .isSameInstanceAs(view.findViewTreeSavedStateRegistryOwner()?.savedStateRegistry)
      }
  }

  @Test
  fun owns() {
    val ownedTreeView = View(composeRule.activity)
    val activityViewTreeOwner =
      ViewTreeOwner.from(composeRule.activity).apply { own(ownedTreeView) }
    assertThat(ViewTreeOwner)
      .transform("of") { it.of(ownedTreeView) }
      .isEqualTo(activityViewTreeOwner)
  }
}
