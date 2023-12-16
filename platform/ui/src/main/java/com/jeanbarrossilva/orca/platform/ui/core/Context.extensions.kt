/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.core

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jeanbarrossilva.orca.platform.ui.core.activity.ActivityStarter
import com.jeanbarrossilva.orca.platform.ui.core.activity.StartableActivity
import java.net.URL

/**
 * Creates an [ActivityStarter] for the [StartableActivity], from which it can be set up and
 * started.
 *
 * @param T [StartableActivity] whose start-up may be configured.
 * @see ActivityStarter.start
 */
inline fun <reified T : StartableActivity> Context.on(): ActivityStarter<T> {
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
