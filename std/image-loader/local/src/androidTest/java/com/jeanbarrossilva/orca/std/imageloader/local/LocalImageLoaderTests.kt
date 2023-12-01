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

package com.jeanbarrossilva.orca.std.imageloader.local

import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class LocalImageLoaderTests {
  @Test
  fun loads() {
    val imageLoader =
      object : LocalImageLoader() {
        override val context = InstrumentationRegistry.getInstrumentation().context
        override val source = R.drawable.ic_white
      }
    runTest {
      assertThat(imageLoader.load(width = 1, height = 1)?.pixels.orEmpty()).all {
        hasSize(1)
        given { assertThat(it.single().x).isEqualTo(0) }
        given { assertThat(it.single().y).isEqualTo(0) }
        given { assertThat(it.single().color).isEqualTo(Color.WHITE) }
      }
    }
  }
}
