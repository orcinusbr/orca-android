package com.jeanbarrossilva.mastodonte.platform.ui.test.component.timeline.toot.time

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import net.time4j.android.ApplicationStarter
import org.junit.rules.ExternalResource

/**
 * [ExternalResource] that initializes the Time4J library before each test.
 *
 * @see ApplicationStarter.initialize
 **/
class Time4JTestRule : ExternalResource() {
    override fun before() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        ApplicationStarter.initialize(application)
    }
}
