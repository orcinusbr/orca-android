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

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.Rule
import org.junit.Test

internal class ErrorDispatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun addsMessagesOnErrorAnnouncements() {
    var messages = emptyList<String>()
    composeRule.setContent {
      val dispatcher = rememberErrorDispatcher { errorAlways("🦭") }

      with(dispatcher.messages) messages@{
        DisposableEffect(this) {
          messages = this@messages
          onDispose {}
        }
      }

      DisposableEffect(Unit) {
        dispatcher.dispatch()
        onDispose {}
      }
    }
    assertThat(messages).containsExactly("🦭")
  }
}
