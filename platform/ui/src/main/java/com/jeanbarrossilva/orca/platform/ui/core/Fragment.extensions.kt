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

import android.app.Application
import androidx.fragment.app.Fragment

/**
 * [Application] to which this [Fragment] is attached.
 *
 * @throws IllegalStateException If it's not attached.
 */
val Fragment.application
  get() =
    activity?.application
      ?: throw IllegalStateException("Fragment $this not attached to an Application.")

/**
 * Gets the argument put with the given [key] lazily.
 *
 * @param key Key to which the argument is associated.
 * @throws ClassCastException If the argument is present but isn't a [T].
 */
fun <T> Fragment.argument(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    requireArguments().get(key) as T
  }
}
