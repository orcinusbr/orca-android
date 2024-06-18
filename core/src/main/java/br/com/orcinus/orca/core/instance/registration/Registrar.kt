/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.instance.registration

import br.com.orcinus.orca.core.InternalCoreApi
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

/**
 * Attempts to register an [Account] in an [Instance] that is both known by the implementation and
 * available.
 *
 * @see register
 */
abstract class Registrar @InternalCoreApi constructor() {
  /** [Domain]s of the [Instance]s in which registration can be attempted to be performed. */
  protected abstract val domains: List<Domain>

  /**
   * Iterates through each of the [Instance]s' [Domain]s that are known by this [Registrar] and
   * tries to register an [Account] in one that is available.
   *
   * **NOTE**: Collecting the resulting [Flow] with an invalid [email] will cause a
   * [Credentials.InvalidEmailException] to be thrown. Conversely, if the [password] is blank, a
   * [Credentials.BlankPasswordException] will be thrown.
   *
   * @param email E-mail address. Ideally would be one that hasn't yet been used by anyone else in
   *   the [Instance].
   * @param password Private key that provides access to the [Account] alongside the [email].
   * @return [Flow] to which the results of registration attempts are emitted. By design, if any of
   *   them is successful, no subsequent [Registration]s are emitted to the returned [Flow].
   * @see Instance.domain
   * @see FlowCollector.emit
   * @see Flow.collect
   */
  fun register(email: String, password: String): Flow<Registration> {
    return flow {
      val domainIterator = domains.iterator()
      val credentials = Credentials(email, password)
      var hasSucceeded = false
      while (!hasSucceeded && domainIterator.hasNext()) {
        val domain = domainIterator.next()
        hasSucceeded = register(credentials, domain)
        emit(Registration(domain, hasSucceeded))
      }
    }
  }

  /**
   * Attempts to register an [Account] in the [Instance] that has the specified [domain].
   *
   * @param credentials [Credentials] for accessing the [Account].
   * @param domain [Domain] of the [Instance] in which registration is being tried.
   * @return `true` if the [Account] has been registered in the [Instance]; otherwise, `false`.
   * @see Instance.domain
   */
  protected abstract suspend fun register(credentials: Credentials, domain: Domain): Boolean
}
