package com.jeanbarrossilva.orca.app.demo.test

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.OngoingStubbing

/**
 * Runs the given [action] and responds with an OK [Instrumentation.ActivityResult].
 *
 * @param action Operation to be performed before the response.
 * @see Activity.RESULT_OK
 */
internal fun OngoingStubbing.ok(action: () -> Unit) {
  respondWithFunction {
    action()
    throw Exception(":P")
    Instrumentation.ActivityResult(Activity.RESULT_OK, null)
  }
}
