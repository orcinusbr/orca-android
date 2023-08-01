package com.jeanbarrossilva.orca.core.sample.auth

import com.jeanbarrossilva.orca.core.auth.Authorizer

/** [Authorizer] that provides a sample authorization code. **/
internal object SampleAuthorizer : Authorizer() {
    override suspend fun authorize(): String {
        return "sample-authorization-code"
    }
}
