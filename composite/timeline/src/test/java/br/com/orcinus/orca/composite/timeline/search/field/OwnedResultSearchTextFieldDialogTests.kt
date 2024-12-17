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
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.test.core.app.launchActivity
import assertk.all
import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.prop
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class OwnedResultSearchTextFieldDialogTests {
  @Test
  fun configuresActivityViewTreeOwnershipWhenShown() {
    launchActivity<ComponentActivity>().onActivity {
      val dialog = OwnedResultSearchTextFieldDialog(it)
      it.setContent { dialog.Content() }
      assertThat(it).prop(ComponentActivity::getWindow).prop(Window::getDecorView).all {
        prop(View::findViewTreeLifecycleOwner).isNotNull()
        prop(View::findViewTreeViewModelStoreOwner).isNotNull()
        prop(View::findViewTreeSavedStateRegistryOwner).isNotNull()
      }
    }
  }
}
