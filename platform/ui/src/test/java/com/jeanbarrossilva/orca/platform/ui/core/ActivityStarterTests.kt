package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Activity
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.any
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class ActivityStarterTests {
    private class TestActivity : Activity()

    @Test
    fun `GIVEN an Activity WHEN starting it THEN it's started`() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val mockedContext = spy(context)
        mockedContext.on<TestActivity>().asNewTask().start()
        verify(mockedContext).startActivity(any())
    }
}
