/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertSame
import org.junit.Test

internal class ViewExtensionsTests {
  private val context
    get() = InstrumentationRegistry.getInstrumentation().context

  @Test
  fun identifiesUnidentifiedView() {
    val view = View(context).apply(View::identify)
    assertNotEquals(View.NO_ID, view.id)
  }

  @Test
  fun doesNotIdentifyIdentifiedView() {
    val id = View.generateViewId()
    val view = View(context).apply { this.id = id }.also(View::identify)
    assertEquals(id, view.id)
  }

  @Test
  fun findsViewWhenSearchingForItFromItselfInclusively() {
    val view =
      LinearLayout(context).apply {
        addView(LinearLayout(context))
        addView(TextView(context))
      }
    assertSame(view, view.get<LinearLayout>())
  }

  @Test
  fun doesNotFindViewWhenSearchingForItFromItselfExclusively() {
    val view =
      LinearLayout(context).apply {
        addView(LinearLayout(context))
        addView(ImageView(context))
      }
    assertNotSame(view, view.get<LinearLayout>(isInclusive = false))
  }

  @Test
  fun findsNestedView() {
    val view = TextView(context).apply { text = "🥸" }
    assertSame(
      view,
      FrameLayout(context)
        .apply {
          addView(
            LinearLayout(context).apply { addView(FrameLayout(context).apply { addView(view) }) }
          )
        }
        .get<TextView>()
    )
  }
}
