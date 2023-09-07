package com.jeanbarrossilva.orca.core.sharedpreferences.actor.test

import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.core.sharedpreferences.actor.SharedPreferencesActorProvider
import org.junit.rules.ExternalResource

internal class SharedPreferencesCoreTestRule : ExternalResource() {
    lateinit var actorProvider: SharedPreferencesActorProvider
        private set

    override fun before() {
        val context = InstrumentationRegistry.getInstrumentation().context
        actorProvider = SharedPreferencesActorProvider(context)
    }

    override fun after() {
        actorProvider.reset()
    }
}
