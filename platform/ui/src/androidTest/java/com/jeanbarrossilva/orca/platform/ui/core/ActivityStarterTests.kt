package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

internal class ActivityStarterTests {
    class TestActivity : Activity()

    @Test
    fun startsActivity() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val spiedContext = spyk(context)
        spiedContext.on<TestActivity>().asNewTask().start()
        verify { spiedContext.startActivity(any()) }
    }
}
