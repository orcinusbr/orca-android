package com.jeanbarrossilva.core.http.android

import android.util.Log
import com.jeanbarrossilva.orca.core.http.Logger

/**
 * [Logger] that uses Android's [Log] for logging.
 *
 * @param tag [String] for identifying the source of a log.
 **/
class AndroidLogger private constructor(private val tag: String) : Logger {
    override fun info(info: String) {
        Log.i(tag, info)
    }

    override fun error(error: String) {
        Log.e(tag, error)
    }

    companion object {
        /**
         * Creates an [AndroidLogger] that tags logs with the given [tag].
         *
         * @param tag [String] for identifying the source of a log.
         **/
        fun taggedAs(tag: String): AndroidLogger {
            return AndroidLogger(tag)
        }
    }
}
