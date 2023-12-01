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

package com.jeanbarrossilva.orca.core.mastodon.auth.authentication.activity

import android.app.Activity
import android.content.Intent

/**
 * Gets this [Activity]'s [Intent][Intent] extra put with the given [key] lazily.
 *
 * @param key Key to which the extra is associated.
 * @throws ClassCastException If the extra is present but isn't a [T].
 */
internal inline fun <reified T> Activity.extra(key: String): Lazy<T> {
  return lazy {
    @Suppress("DEPRECATION")
    intent?.extras?.get(key) as T
  }
}
