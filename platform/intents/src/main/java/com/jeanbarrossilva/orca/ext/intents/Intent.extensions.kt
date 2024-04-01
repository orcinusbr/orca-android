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

package com.jeanbarrossilva.orca.ext.intents

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
 * Creates an [Intent] through which the [Activity] can be started.
 *
 * @param context [Context] to create the [Intent] with.
 * @param args Arguments to be passed to the [Intent]'s [extras][Intent.getExtras].
 * @see Context.startActivity
 */
inline fun <reified T : Activity> intentOf(
  context: Context,
  vararg args: Pair<String, Any?>
): Intent {
  val extras = bundleOfOrNull(*args)
  return Intent(context, T::class.java).apply { putExtras(extras) }
}

/**
 * Creates an [Intent] that allows the user to share the [text] externally.
 *
 * @param text Content to be shared.
 */
fun intentOf(text: String): Intent {
  return Intent(Intent.ACTION_SEND).apply {
    flags = Intent.FLAG_ACTIVITY_NEW_TASK
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, text)
  }
}
