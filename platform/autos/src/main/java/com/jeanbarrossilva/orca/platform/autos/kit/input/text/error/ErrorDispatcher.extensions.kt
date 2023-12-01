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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

/** Messages of the errors that have been announced by this [ErrorDispatcher]. */
internal val ErrorDispatcher.messages: SnapshotStateList<String>
  @Composable
  get() {
    val errors = remember(this) { mutableStateListOf<String>() }
    val onAnnouncementListener =
      ErrorDispatcher.OnAnnouncementListener {
        errors.clear()
        errors.addAll(it)
      }

    DisposableEffect(this) {
      listen(onAnnouncementListener)
      onDispose { remove(onAnnouncementListener) }
    }

    return errors
  }

/**
 * [State] that holds whether this [ErrorDispatcher] contains any errors related to the text that's
 * been registered into it the most recently.
 */
val ErrorDispatcher.containsErrorsAsState: State<Boolean>
  @Composable
  get() {
    val state = remember(this) { mutableStateOf(containsErrors) }
    val onAnnouncementListener =
      ErrorDispatcher.OnAnnouncementListener { state.value = containsErrors }

    DisposableEffect(this) {
      listen(onAnnouncementListener)
      onDispose { remove(onAnnouncementListener) }
    }

    return state
  }

/**
 * Remembers an [ErrorDispatcher].
 *
 * @param build Configures the [ErrorDispatcher] to be built and returned through its
 *   [ErrorDispatcher.Builder].
 */
@Composable
fun rememberErrorDispatcher(build: ErrorDispatcher.Builder.() -> Unit = {}): ErrorDispatcher {
  val errorDispatcher = remember(build) { buildErrorDispatcher(build) }
  DisposableEffect(Unit) { onDispose(errorDispatcher::reset) }
  return errorDispatcher
}

/**
 * Builds an [ErrorDispatcher].
 *
 * @param build Configures the [ErrorDispatcher] to be built and returned through its
 *   [ErrorDispatcher.Builder].
 */
internal fun buildErrorDispatcher(build: ErrorDispatcher.Builder.() -> Unit = {}): ErrorDispatcher {
  return ErrorDispatcher.Builder().apply(build).build()
}
