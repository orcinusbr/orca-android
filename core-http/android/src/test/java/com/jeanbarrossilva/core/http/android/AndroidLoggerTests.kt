package com.jeanbarrossilva.core.http.android

import android.util.Log
import org.junit.Test
import org.mockito.Mockito.mockStatic

internal class AndroidLoggerTests {
    private val logger = AndroidLogger.taggedAs(TAG)

    @Test
    fun `GIVEN an info WHEN logging it THEN Log's i method is called`() {
        mockStatic(Log::class.java).use {
            logger.info("😮")
            it.verify { Log.i(TAG, "😮") }
        }
    }

    @Test
    fun `GIVEN an error WHEN logging it THEN Log's e method is called`() {
        mockStatic(Log::class.java).use {
            logger.error("😵")
            it.verify { Log.e(TAG, "😵") }
        }
    }

    companion object {
        private const val TAG = "AndroidLoggerTests"
    }
}
