package com.jeanbarrossilva.orca.core.sample.rule

import com.jeanbarrossilva.orca.core.sample.SampleCoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.SampleProfileWriter
import com.jeanbarrossilva.orca.core.sample.feed.profile.toot.SampleTootWriter
import com.jeanbarrossilva.orca.std.injector.Injector
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that registers a [SampleCoreModule] at the start of each test and unregisters
 * and resets the sample writers (such as [SampleProfileWriter] and [SampleTootWriter]) when they're
 * finished.
 */
class SampleCoreTestRule : ExternalResource() {
  override fun before() {
    Injector.register(SampleCoreModule { TestSampleImageLoader.Provider })
  }

  override fun after() {
    SampleProfileWriter.reset()
    SampleTootWriter.reset()
    Injector.unregister<SampleCoreModule>()
  }
}
