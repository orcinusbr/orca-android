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

package br.com.orcinus.orca.core.mastodon.instance.registration

import br.com.orcinus.orca.core.auth.Authorizer
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.instance.registration.interactor.MastodonSocialRegistrationWebpageInteractor

/**
 * Provides a [MastodonRegistrationWebpageInteractor] for registering an [Account].
 *
 * @see provide
 */
internal enum class MastodonRegistrationWebpageInteractorProvider {
  /**
   * Provides a [mastodon.social](https://mastodon.social) [MastodonRegistrationWebpageInteractor].
   */
  MastodonSocial {
    override val domain = MastodonSocialRegistrationWebpageInteractor.domain

    override fun provide(): MastodonRegistrationWebpageInteractor {
      return MastodonSocialRegistrationWebpageInteractor()
    }
  };

  /**
   * [Domain] of the [Instance] in which the provided [MastodonRegistrationWebpageInteractor]
   * attempts to register an [Account].
   *
   * @see Instance.domain
   */
  protected abstract val domain: Domain

  /**
   * Provides a [MastodonRegistrationWebpageInteractor] that attempts to register an [Account] in an
   * [Instance] with the specified [domain].
   */
  abstract fun provide(): MastodonRegistrationWebpageInteractor

  companion object {
    /**
     * [Domain]s of the [Instance]s in which the provided [MastodonRegistrationWebpageInteractor]s
     * can register an [Account].
     */
    val domains = entries.map(MastodonRegistrationWebpageInteractorProvider::domain)

    /**
     * [IllegalStateException] if the [Domain] isn't that of a
     * [MastodonRegistrationWebpageInteractorProvider].
     *
     * @param domain Unknown [Domain] for which an [Authorizer] has been requested to be provided.
     */
    class UnknownDomainException(domain: Domain) :
      IllegalStateException("\"$domain\" isn't a known domain.")

    /**
     * Obtains the [MastodonRegistrationWebpageInteractorProvider] that provides the
     * [MastodonRegistrationWebpageInteractor] for registering an [Account] at the [Instance] to
     * which the given [domain] belongs.
     *
     * @param domain [Domain] of the [Instance] in which the provided
     *   [MastodonRegistrationWebpageInteractor] attempts to register an [Account].
     * @throws UnknownDomainException If the [domain] isn't that of a
     *   [MastodonRegistrationWebpageInteractorProvider].
     * @see MastodonRegistrationWebpageInteractorProvider.provide
     * @see Instance.domain
     * @see MastodonRegistrationWebpageInteractorProvider.domain
     */
    @Throws(UnknownDomainException::class)
    fun at(domain: Domain): MastodonRegistrationWebpageInteractorProvider {
      return entries.find { it.domain == domain } ?: throw UnknownDomainException(domain)
    }
  }
}
