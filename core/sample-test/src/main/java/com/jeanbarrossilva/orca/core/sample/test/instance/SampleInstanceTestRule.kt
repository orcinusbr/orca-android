package com.jeanbarrossilva.orca.core.sample.test.instance

import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.instance.SampleInstance
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that resets the [Instance.Companion.sample]'s writers (such as
 * [SampleInstance.profileWriter] and [SampleInstance.tootWriter]) at the end of every test.
 */
class SampleInstanceTestRule(private val instance: SampleInstance = Instance.sample) :
  ExternalResource() {
  override fun after() {
    Instance.sample.profileWriter.reset()
    Instance.sample.tootWriter.reset()
  }
}
