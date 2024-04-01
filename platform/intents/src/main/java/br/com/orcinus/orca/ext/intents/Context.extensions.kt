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

package br.com.orcinus.orca.ext.intents

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URL

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

/**
 * Opens the share sheet so that the [text] can be shared.
 *
 * @param text Text to be shared.
 */
fun Context.share(text: String) {
  val intent = intentOf(text)
  startActivity(intent)
}
