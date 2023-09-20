package com.jeanbarrossilva.orca.core.http

import android.util.Log

/** Logs messages with different severity levels. **/
internal interface Logger {
    /**
     * Logs additional information.
     *
     * @param info [String] with the information to be logged.
     **/
    fun info(info: String)

    /**
     * Logs the occurrence of an error.
     *
     * @param error [String] describing the error to be logged.
     **/
    fun error(error: String)

    companion object {
        /** Tag to which logs sent by [android] will be attached. **/
        const val ANDROID_LOGGER_TAG = "CoreHttpClient"

        /** [Logger] that uses the Android [Log]. **/
        val android = object : Logger {
            override fun info(info: String) {
                Log.i(ANDROID_LOGGER_TAG, info)
            }

            override fun error(error: String) {
                Log.e(ANDROID_LOGGER_TAG, error)
            }
        }
    }
}
