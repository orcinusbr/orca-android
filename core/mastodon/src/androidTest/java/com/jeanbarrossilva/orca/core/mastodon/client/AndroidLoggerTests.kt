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

package com.jeanbarrossilva.orca.core.mastodon.client

import android.util.Log
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Test

internal class AndroidLoggerTests {
  @Test
  fun infoCallsAndroidLogI() {
    mockkStatic(Log::class) {
      Logger.android.info("😮")
      verify { Log.i(Logger.ANDROID_LOGGER_TAG, "😮") }
    }
  }

  @Test
  fun errorCallsAndroidLogE() {
    mockkStatic(Log::class) {
      Logger.android.error("😵")
      verify { Log.e(Logger.ANDROID_LOGGER_TAG, "😵") }
    }
  }
}
