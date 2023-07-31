package com.jeanbarrossilva.mastodonte.core.sample.feed.profile.test

import com.jeanbarrossilva.mastodonte.core.sample.feed.profile.SampleProfileWriter
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that [reset][SampleProfileWriter.reset]s the [SampleProfileWriter] at the end
 * of each test.
 **/
class SampleProfileWriterTestRule : ExternalResource() {
    override fun after() {
        SampleProfileWriter.reset()
    }
}
