package com.jeanbarrossilva.orca.std.injector.test

import com.jeanbarrossilva.orca.std.injector.Injector
import org.junit.rules.ExternalResource

/**
 * Rule for running the [injection] before and clearing the [Injector] after the test is done.
 *
 * @param injection Operation to be performed on the [Injector].
 */
class InjectorTestRule(private val injection: Injector.() -> Unit = {}) : ExternalResource() {
  override fun before() {
    Injector.injection()
  }

  override fun after() {
    Injector.clear()
  }

  /**
   * Surrounds the execution of the [use] lambda with a call to [before] that precedes it and
   * another to [after] that succeeds it.
   *
   * @param use Operation to be performed.
   */
  internal fun use(use: () -> Unit = {}) {
    before()
    use()
    after()
  }
}
