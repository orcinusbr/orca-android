package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isSameAs
import kotlin.test.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest

internal class MutableStateFlowExtensionsTests {
  @Test
  fun notifierFlowHasInitialNotifierAsItsInitialValue() {
    assertThat(notifierFlow().value).isSameAs(Notifier.initial)
  }

  @Test
  fun notifierFlowReceivesSubsequentNotifierWhenNotifiedWithInitialAsItsValue() {
    val flow = notifierFlow()
    runTest {
      flow.test {
        awaitItem()
        flow.notify()
        assertThat(awaitItem()).isSameAs(Notifier.subsequent())
      }
    }
  }

  @Test
  fun notifierFlowReceivesInitialNotifierWhenNotifiedWithSubsequentAsItsValue() {
    val flow = notifierFlow().apply(MutableStateFlow<Notifier>::notify)
    runTest {
      flow.test {
        awaitItem()
        flow.notify()
        assertThat(awaitItem()).isSameAs(Notifier.initial)
      }
    }
  }
}
