package com.jeanbarrossilva.mastodonte.core.sample.auth

import com.jeanbarrossilva.mastodonte.core.auth.Authorizer

/** [Authorizer] that provides a sample authorization code. **/
object SampleAuthorizer : Authorizer() {
    fun authorize(): String {
        return "sample-authorization-code"
    }
}
