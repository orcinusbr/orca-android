package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.pagination

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.jvm.optionals.getOrNull
import kotlin.test.Test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

internal class FlowExtensionsTests {
  @Test
  fun associates() {
    runTest {
      flowOf(8, 12)
        .associateWith { it * it }
        .test {
          assertThat(awaitItem()).isEqualTo(64 to 8)
          assertThat(awaitItem()).isEqualTo(144 to 12)
          awaitComplete()
        }
    }
  }

  @Test
  fun mapsConditionally() {
    runTest {
      flowOf(32, 64, 105, 128)
        .map({ it % 2 == 0 }) { it * 2 }
        .test {
          assertThat(awaitItem()).isEqualTo(64)
          assertThat(awaitItem()).isEqualTo(128)
          assertThat(awaitItem()).isEqualTo(105)
          assertThat(awaitItem()).isEqualTo(256)
          awaitComplete()
        }
    }
  }

  @Test
  fun comparesNotNull() {
    runTest {
      flowOf(2, 3)
        .compareNotNull { previous, current -> (previous.getOrNull() ?: current) * current }
        .test {
          assertThat(awaitItem()).isEqualTo(4)
          assertThat(awaitItem()).isEqualTo(6)
          awaitComplete()
        }
    }
  }
}
