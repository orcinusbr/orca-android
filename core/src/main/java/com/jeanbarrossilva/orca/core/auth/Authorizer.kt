package com.jeanbarrossilva.orca.core.auth

/** Authorizes the user through [authorize]. **/
abstract class Authorizer {
    /**
     * Authorizes the user, allowing the application to perform operations on their behalf.
     *
     * @return Resulting authorization code.
     **/
    @Suppress("FunctionName")
    internal suspend fun _authorize(): String {
        return authorize()
    }

    /**
     * Authorizes the user, allowing the application to perform operations on their behalf.
     *
     * @return Resulting authorization code.
     **/
    protected abstract suspend fun authorize(): String

    companion object
}
