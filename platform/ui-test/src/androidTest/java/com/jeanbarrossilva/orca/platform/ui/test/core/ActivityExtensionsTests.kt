package com.jeanbarrossilva.orca.platform.ui.test.core

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.Test

internal class ActivityExtensionsTests {
  open class AlwaysFocusedActivityWithoutContentView : FragmentActivity() {
    var focusRequestResult: FocusRequestResult = FocusRequestResult.IDLE
      private set

    enum class FocusRequestResult {
      IDLE,
      SUCCEEDED,
      FAILED
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
      super.onWindowFocusChanged(hasFocus)
      tryToRequestFocus(hasFocus)
    }

    private fun tryToRequestFocus(isFocused: Boolean) {
      focusRequestResult =
        try {
          requestFocus(isFocused)
          FocusRequestResult.SUCCEEDED
        } catch (_: IllegalStateException) {
          FocusRequestResult.FAILED
        }
    }
  }

  class AlwaysFocusedActivityWithContentView : AlwaysFocusedActivityWithoutContentView() {
    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(View(this))
    }
  }

  class WindowFocusStealerActivity : Activity()

  @Test
  fun throwsWhenRequestingFocusForActivityWithoutContentViews() {
    lateinit var activity: AlwaysFocusedActivityWithoutContentView
    launchActivity<AlwaysFocusedActivityWithoutContentView>().use { scenario ->
      scenario.onActivity { activity = it }
      launchActivity<WindowFocusStealerActivity>().use {
        assertThat(activity.focusRequestResult)
          .isEqualTo(AlwaysFocusedActivityWithoutContentView.FocusRequestResult.FAILED)
      }
    }
  }

  @Test
  fun requestsFocus() {
    lateinit var activity: AlwaysFocusedActivityWithContentView
    launchActivity<AlwaysFocusedActivityWithContentView>().use { scenario ->
      scenario.onActivity { activity = it }
      launchActivity<WindowFocusStealerActivity>().use {
        assertThat(activity.focusRequestResult)
          .isEqualTo(AlwaysFocusedActivityWithoutContentView.FocusRequestResult.SUCCEEDED)
      }
    }
  }
}
