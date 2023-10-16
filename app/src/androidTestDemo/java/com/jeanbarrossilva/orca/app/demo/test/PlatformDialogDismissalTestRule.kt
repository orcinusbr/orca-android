package com.jeanbarrossilva.orca.app.demo.test

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.asCompletableFuture
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.rules.ExternalResource

class PlatformDialogDismissalTestRule : ExternalResource() {
  override fun before() {
    runTest { dismissPlatformDialogInstantlyIfShown(this) }
  }

  private fun dismissPlatformDialogInstantlyIfShown(coroutineScope: CoroutineScope) {
    try {
      dismissPlatformDialogInstantly(coroutineScope)
    } catch (_: TimeoutException) {} catch (_: ExecutionException) {}
  }

  private fun dismissPlatformDialogInstantly(coroutineScope: CoroutineScope) {
    return coroutineScope
      .launch { dismissPlatformDialog() }
      .asCompletableFuture()
      .orTimeout(0, TimeUnit.MILLISECONDS)
      .get()
  }

  private fun dismissPlatformDialog() {
    Espresso.onView(ViewMatchers.isRoot())
      .noActivity()
      .inRoot(RootMatchers.isDialog())
      .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
      .perform(ViewActions.click())
  }
}
