package com.jeanbarrossilva.mastodonte.core.test

import com.jeanbarrossilva.mastodonte.core.auth.Authorizer

/**
 * [Authorizer] that provides a fixed authorization code.
 *
 * @param onAuthorize Operation to be performed when [authorize] is called.
 * @see AUTHORIZATION_CODE
 **/
class TestAuthorizer(private val onAuthorize: () -> Unit = { }) : Authorizer() {
    override suspend fun authorize(): String {
        onAuthorize()
        return AUTHORIZATION_CODE
    }

    companion object {
        /** Authorization code provided by [authorize]. **/
        const val AUTHORIZATION_CODE = "authorization-code"
    }
}
