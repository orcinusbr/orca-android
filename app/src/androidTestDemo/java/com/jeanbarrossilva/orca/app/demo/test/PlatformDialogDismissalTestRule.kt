package com.jeanbarrossilva.orca.app.demo.test

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.time.withTimeout
import kotlinx.coroutines.withTimeout
import org.junit.rules.ExternalResource

class PlatformDialogDismissalTestRule : ExternalResource() {
  override fun before() {
    runTest { dismissPlatformDialogInstantly() }
  }

  private suspend fun dismissPlatformDialogInstantly() {
    try {
      withTimeout(0) { dismissPlatformDialog() }
    } catch (_: TimeoutCancellationException) {}
  }

  private fun dismissPlatformDialog() {
    Espresso.onView(ViewMatchers.isRoot())
      .noActivity()
      .inRoot(RootMatchers.isDialog())
      .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
      .perform(ViewActions.click())
  }
}
