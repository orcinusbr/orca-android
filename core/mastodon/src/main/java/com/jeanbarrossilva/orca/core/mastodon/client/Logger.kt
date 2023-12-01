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

/** Logs messages with different severity levels. */
abstract class Logger {
  /**
   * Logs additional information.
   *
   * @param info [String] with the information to be logged.
   */
  internal fun info(info: String) {
    onInfo(info)
  }

  /**
   * Logs the occurrence of an error.
   *
   * @param error [String] describing the error to be logged.
   */
  internal fun error(error: String) {
    onError(error)
  }

  /**
   * Logs additional information.
   *
   * @param info [String] with the information to be logged.
   */
  protected abstract fun onInfo(info: String)

  /**
   * Logs the occurrence of an error.
   *
   * @param error [String] describing the error to be logged.
   */
  protected abstract fun onError(error: String)

  companion object {
    /** Tag to which logs sent by [android] will be attached. */
    const val ANDROID_LOGGER_TAG = "CoreHttpClient"

    /** [Logger] that uses the Android [Log]. */
    val android =
      object : Logger() {
        override fun onInfo(info: String) {
          Log.i(ANDROID_LOGGER_TAG, info)
        }

        override fun onError(error: String) {
          Log.e(ANDROID_LOGGER_TAG, error)
        }
      }
  }
}
