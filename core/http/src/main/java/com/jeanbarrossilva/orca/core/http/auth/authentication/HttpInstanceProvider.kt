package com.jeanbarrossilva.orca.core.http.auth.authentication

import com.jeanbarrossilva.orca.core.http.instance.HttpInstance
import com.jeanbarrossilva.orca.core.http.instance.SomeHttpInstance
import com.jeanbarrossilva.orca.core.instance.domain.Domain

/** Provides an [HttpInstance] through [onProvide]. */
abstract class HttpInstanceProvider {
  /**
   * Provides an [HttpInstance].
   *
   * @param domain [Domain] to which the user belongs.
   */
  internal fun provide(domain: Domain): SomeHttpInstance {
    return onProvide(domain)
  }

  /**
   * Provides an [HttpInstance].
   *
   * @param domain [Domain] to which the user belongs.
   */
  protected abstract fun onProvide(domain: Domain): SomeHttpInstance
}
