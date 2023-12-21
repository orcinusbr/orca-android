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

package com.jeanbarrossilva.orca.ext.intents.test

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry

/**
 * [InstrumentationRegistry]'s [Instrumentation]'s [Context] by hereby declared operations will be
 * performed.
 *
 * @see InstrumentationRegistry.getInstrumentation
 * @see Instrumentation.getContext
 */
private val context: Context
  get() = InstrumentationRegistry.getInstrumentation().context

/**
 * Browses to the [uri] from [InstrumentationRegistry]'s [Instrumentation]'s [Context].
 *
 * @param uri [String] representation of the [Uri] to which the browsing will be performed.
 * @see InstrumentationRegistry.getInstrumentation
 * @see Instrumentation.getContext
 */
internal fun browseTo(uri: String) {
  context.startActivity(
    Intent(Intent.ACTION_VIEW).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setData(Uri.parse(uri))
  )
}

/**
 * Starts the specified [Activity] from [InstrumentationRegistry]'s [Instrumentation]'s [Context].
 *
 * @param T [Activity] to be started.
 * @see InstrumentationRegistry.getInstrumentation
 * @see Instrumentation.getContext
 */
internal inline fun <reified T : Activity> startActivity() {
  context.startActivity(Intent(context, T::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}
