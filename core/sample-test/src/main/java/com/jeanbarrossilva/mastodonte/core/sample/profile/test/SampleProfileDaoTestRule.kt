package com.jeanbarrossilva.mastodonte.core.sample.profile.test

import com.jeanbarrossilva.mastodonte.core.sample.profile.SampleProfileDao
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that [clear][SampleProfileDao.reset]s the [SampleProfileDao] at the end of
 * each test.
 **/
class SampleProfileDaoTestRule : ExternalResource() {
    override fun after() {
        SampleProfileDao.reset()
    }
}
