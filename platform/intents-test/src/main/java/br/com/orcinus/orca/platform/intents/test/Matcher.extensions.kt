/*
 * Copyright © 2023-2024 Orcinus
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

import android.content.Intent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import java.net.URI
import org.hamcrest.CoreMatchers.both
import org.hamcrest.Matcher

/**
 * Creates a [Matcher] that matches an [Intent] that browses to the given [uri].
 *
 * @param uri [String] form of the [URI] to which the [Intent] browses.
 */
@PublishedApi
internal fun browsesTo(uri: String): Matcher<Intent> {
  return both(hasAction(Intent.ACTION_VIEW)).and(hasData(uri))
}
