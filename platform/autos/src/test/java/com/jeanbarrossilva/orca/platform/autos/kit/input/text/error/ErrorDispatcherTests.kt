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

package com.jeanbarrossilva.orca.platform.autos.kit.input.text.error

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

internal class ErrorDispatcherTests {
  @Test
  fun doesNotAnnounceErrorsBeforeDispatch() {
    var hasBeenAnnounced = false
    val onAnnouncementListener = ErrorDispatcher.OnAnnouncementListener { hasBeenAnnounced = true }
    buildErrorDispatcher { errorAlways("⚠️") }
      .apply {
        use {
          listen(onAnnouncementListener)
          register("🐋")
        }
      }
    assertThat(hasBeenAnnounced).isFalse()
  }

  @Test
  fun dispatches() {
    val dispatcher =
      buildErrorDispatcher().apply {
        register("🍨")
        dispatch()
      }
    assertThat(dispatcher.hasDispatched).isTrue()
  }

  @Test
  fun announcesEncounteredErrorsWhenDispatched() {
    var hasErrorBeenAnnounced = false
    val onAnnouncementListener =
      ErrorDispatcher.OnAnnouncementListener { hasErrorBeenAnnounced = true }
    buildErrorDispatcher { errorAlways("😷") }
      .use { dispatcher ->
        dispatcher.listen(onAnnouncementListener)
        dispatcher.register("🐳")
        dispatcher.dispatch()
      }
    assertThat(hasErrorBeenAnnounced).isTrue()
  }

  @Test
  fun resetsAfterUsage() {
    val dispatcher =
      buildErrorDispatcher { errorAlways("🇳🇦") }
        .apply {
          use {
            listen {}
            register("🍕")
            dispatch()
          }
        }
    assertThat(dispatcher.hasDispatched).isFalse()
  }
}
