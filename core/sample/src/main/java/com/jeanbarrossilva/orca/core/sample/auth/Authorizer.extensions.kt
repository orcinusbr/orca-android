package com.jeanbarrossilva.orca.core.sample.auth

import com.jeanbarrossilva.orca.core.auth.Authorizer

/** [Authorizer] returned by [sample]. */
private val sampleAuthorizer =
  object : Authorizer() {
    override suspend fun authorize(): String {
      return "sample-authorization-code"
    }
  }

/** [Authorizer] that provides a sample authorization code. */
val Authorizer.Companion.sample
  get() = sampleAuthorizer
