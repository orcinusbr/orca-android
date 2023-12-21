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

package com.jeanbarrossilva.orca.ext.intents

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.OngoingStubbing
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import junit.framework.AssertionFailedError
import org.hamcrest.Matcher

/**
 * Asserts that an [Intent] for browsing to the given [String] representation of an [Uri] is started
 * after the [action] lambda is run. The [Intent] responds with an "OK".
 *
 * @param uri [String] version of the [Uri] to which browsing should be requested.
 * @param action Callback that requests the browsing.
 * @throws AssertionFailedError If browsing to the [uri] is not requested or it is done so multiple
 *   times.
 */
@Throws(AssertionFailedError::class)
inline fun intendBrowsingTo(uri: String, action: () -> Unit) {
  intend(browsesTo(uri), action)
}

/**
 * Asserts that the given [Activity] is requested to be started after the [action] is performed, and
 * makes the [Intent] respond with an "OK".
 *
 * @param T [Activity] whose start is expected to be requested.
 * @param action Callback that requests the [Activity] to be started.
 * @throws AssertionFailedError If the [Activity] is not requested to be started or it is done so
 *   multiple times.
 */
@Throws(AssertionFailedError::class)
inline fun <reified T : Activity> intendStartingOf(action: () -> Unit) {
  intend(hasComponent(T::class.java.name), action)
}

/**
 * Asserts that an [Intent] that is matched by the given [matcher] is started within the [action]
 * lambda. When it is, it simply responds with an "OK".
 *
 * Should be called once per test case.
 *
 * @param matcher [Matcher] that matches the [Intent] that is expected to be started.
 * @param action Callback that launches the intended [Intent].
 * @throws AssertionFailedError If none or multiple [Intent]s are matched.
 * @see OngoingStubbing.respondWithOK
 */
@PublishedApi
@Throws(AssertionFailedError::class)
internal inline fun intend(matcher: Matcher<Intent>, action: () -> Unit) {
  Intents.init()
  try {
    intending(matcher).respondWithOK()
    action()
    intended(matcher)
  } finally {
    Intents.release()
  }
}
