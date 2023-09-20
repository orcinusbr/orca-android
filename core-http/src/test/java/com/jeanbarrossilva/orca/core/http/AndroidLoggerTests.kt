package com.jeanbarrossilva.orca.core.http

import android.util.Log
import org.junit.Test
import org.mockito.Mockito.mockStatic

internal class AndroidLoggerTests {
    @Test
    fun `GIVEN an info WHEN logging it THEN Log's i method is called`() {
        mockStatic(Log::class.java).use {
            Logger.android.info("😮")
            it.verify { Log.i(Logger.ANDROID_LOGGER_TAG, "😮") }
        }
    }

    @Test
    fun `GIVEN an error WHEN logging it THEN Log's e method is called`() {
        mockStatic(Log::class.java).use {
            Logger.android.error("😵")
            it.verify { Log.e(Logger.ANDROID_LOGGER_TAG, "😵") }
        }
    }
}
