package com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier

import assertk.assertThat
import assertk.assertions.isSameAs
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test.access
import com.jeanbarrossilva.orca.feature.postdetails.viewmodel.notifier.test.get
import kotlin.test.Test

internal class NotifierTests {
  @Test(expected = IllegalArgumentException::class)
  fun throwsWhenGettingSuccessorOfUnknownNotifier() {
    val unknown = Notifier::class.constructors[String::class].access { call("Notifier.unknown") }
    Notifier.after(unknown)
  }

  @Test
  fun subsequentIsSuccessorOfInitial() {
    assertThat(Notifier.after(Notifier.initial)).isSameAs(Notifier.subsequent())
  }

  @Test
  fun initialIsSuccessorOfSubsequent() {
    assertThat(Notifier.after(Notifier.subsequent())).isSameAs(Notifier.initial)
  }
}
