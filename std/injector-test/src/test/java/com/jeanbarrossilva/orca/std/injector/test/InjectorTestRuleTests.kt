package com.jeanbarrossilva.orca.std.injector.test

import com.jeanbarrossilva.orca.std.injector.Injector
import kotlin.test.Test

internal class InjectorTestRuleTests {
  @Test(expected = NoSuchElementException::class)
  fun clears() {
    InjectorTestRule().use { Injector.inject { 0 } }
    Injector.get<Int>()
  }
}
