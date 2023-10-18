package com.jeanbarrossilva.orca.app.demo.test

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.OngoingStubbing

/**
 * Responds with an OK [Instrumentation.ActivityResult].
 *
 * @see Activity.RESULT_OK
 */
internal fun OngoingStubbing.respondWithOK() {
  val activityResult = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
  respondWith(activityResult)
}
