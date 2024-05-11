/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.network.requester.client

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
