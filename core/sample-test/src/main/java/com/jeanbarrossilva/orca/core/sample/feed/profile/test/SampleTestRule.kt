package com.jeanbarrossilva.orca.core.sample.feed.profile.test

import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootWriter
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that resets the sample writers (such as [SampleProfileWriter] and
 * [SampleTootWriter]) at the end of each test.
 */
class SampleTestRule : ExternalResource() {
  override fun after() {
    SampleProfileWriter.reset()
    SampleTootWriter.reset()
  }
}
