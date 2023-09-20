package com.jeanbarrossilva.orca.core.http

/** Logs messages with different severity levels. **/
interface Logger {
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
}
