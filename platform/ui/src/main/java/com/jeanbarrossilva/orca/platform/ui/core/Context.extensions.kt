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
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URL

/**
 * Creates an [ActivityStarter] for the [Activity], from which it can be set up and started.
 *
 * @param T [Activity] whose start-up may be configured.
 * @see ActivityStarter.start
 */
inline fun <reified T : Activity> Context.on(): ActivityStarter<T> {
  return ActivityStarter(this, T::class)
}

/**
 * Browses to the [url].
 *
 * @param url [URL] to browse to.
 */
fun Context.browseTo(url: URL) {
  val uri = Uri.parse("$url")
  val intent = Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
  startActivity(intent)
}
