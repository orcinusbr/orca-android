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

package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

internal class ActivityStarterTests {
  class TestActivity : Activity()

  @Test
  fun startsActivity() {
    val context = InstrumentationRegistry.getInstrumentation().context
    val spiedContext = spyk(context)
    spiedContext.on<TestActivity>().asNewTask().start()
    verify { spiedContext.startActivity(any()) }
  }
}
