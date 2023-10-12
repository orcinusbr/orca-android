package com.jeanbarrossilva.orca.core.feed.profile.toot.stat.toggleable

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

internal class ToggleableStatExtensionsTests {
  @Test
  fun buildsToggleableStatWithConfiguredSetEnabled() {
    var isEnabled = false
    runTest {
      ToggleableStat<Int>(count = 1) { setEnabled { isEnabled = it } }
        .apply {
          disable()
          enable()
        }
    }
    assertTrue(isEnabled)
  }
}
