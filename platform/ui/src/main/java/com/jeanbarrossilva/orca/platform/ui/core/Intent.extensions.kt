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
import android.os.Bundle

/**
 * Puts [extras] into this [Intent] if it isn't `null`.
 *
 * @param extras Extras [Bundle] to be put.
 */
@PublishedApi
internal fun Intent.putExtras(extras: Bundle?): Intent {
  return extras?.let(::putExtras) ?: this
}

/**
 * [Intent] through which the [Activity] can be started.
 *
 * @param context [Context] to create the [Intent] with.
 * @param args Arguments to be passed to the [Intent]'s [extras][Intent.getExtras].
 * @see Context.startActivity
 */
inline fun <reified T : Activity> Intent(
  context: Context,
  vararg args: Pair<String, Any?>
): Intent {
  val extras = bundleOf(*args)
  return Intent(context, T::class.java).apply { putExtras(extras) }
}

/**
 * [Intent] that allows the user to share the [text] externally.
 *
 * @param text Content to be shared.
 */
fun Intent(text: String): Intent {
  return Intent(Intent.ACTION_SEND).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, text)
  }
}
