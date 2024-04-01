/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.platform.intents.test

import android.content.Context
import android.content.Intent
import android.net.Uri
import br.com.orcinus.orca.platform.starter.StartableActivity
import br.com.orcinus.orca.platform.starter.on
import br.com.orcinus.orca.platform.testing.context

/**
 * Browses to the URI from the test's [Context].
 *
 * @param uriAsString [String] representation of the [Uri] to which the browsing will be performed.
 * @see context
 */
internal fun browseTo(uriAsString: String) {
  val uri = Uri.parse(uriAsString)
  val intent = Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(uri)
  context.startActivity(intent)
}

/**
 * Starts a [StartableActivity] from the test's [Context].
 *
 * @see context
 */
internal fun startActivity() {
  context.on<StartableActivity>().asNewTask().start()
}
