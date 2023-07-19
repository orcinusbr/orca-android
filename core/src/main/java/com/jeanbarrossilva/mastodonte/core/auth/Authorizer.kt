package com.jeanbarrossilva.mastodonte.core.auth

/** Authorizes the user through [authorize]. **/
abstract class Authorizer {
    /**
     * Authorizes the user, allowing the application to perform operations on their behalf.
     *
     * @return Resulting authorization code.
     **/
    internal abstract suspend fun authorize(): String
}
