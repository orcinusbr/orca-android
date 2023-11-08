package com.jeanbarrossilva.orca.app.test

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.platform.ui.core.Intent

/** Launches a [TestOrcaActivity]. */
internal fun launchOrcaActivity(): ActivityScenario<TestOrcaActivity> {
  val context = InstrumentationRegistry.getInstrumentation().targetContext
  val intent = Intent<TestOrcaActivity>(context)
  return launchActivity(intent)
}
