package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier

import assertk.assertThat
import assertk.assertions.isNotEqualTo
import assertk.assertions.isSameAs
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test.access
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test.get
import kotlin.test.Test

internal class NotifierTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenGettingSuccessorOfUnknownNotifier() {
    Notifier::class.constructors[String::class].access { call("Notifier.unknown") }.next()
  }

  @Test
  fun subsequentIsSuccessorOfInitial() {
    assertThat(Notifier.initial.next()).isNotEqualTo(Notifier.initial)
  }

  @Test
  fun initialIsSuccessorOfSubsequent() {
    assertThat(Notifier.initial.next().next()).isSameAs(Notifier.initial)
  }
}
