/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.instance.requester.authentication

import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import br.com.orcinus.orca.core.mastodon.instance.requester.Logger
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.std.func.monad.flatten
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.bearerAuth
import io.ktor.client.statement.HttpResponse
import io.ktor.http.content.PartData
import java.net.URI

/**
 * [Requester] whose requests require authentication in order to be performed.
 *
 * @property logger [Logger] by which received [HttpResponse]s will be logged.
 * @property clientEngineFactory [HttpClientEngineFactory] that creates the [HttpClientEngine]
 *   powering the underlying [client].
 * @property baseURI [URI] from which routes are constructed.
 * @property lock [AuthenticationLock] for unlocking requests made by an unauthenticated [Actor].
 */
private class AuthenticatedRequester
@InternalRequesterApi
constructor(
  logger: Logger,
  baseURI: URI,
  clientEngineFactory: HttpClientEngineFactory<*>,
  val lock: SomeAuthenticationLock
) :
  Requester<AuthenticationLock.FailedAuthenticationException>(
    logger,
    baseURI,
    clientEngineFactory
  ) {
  override suspend fun delete(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ) =
    lock
      .scheduleUnlock {
        super.delete(
          config,
          route,
          build = {
            bearerAuth(it.accessToken)
            build()
          }
        )
      }
      .flatten()

  override suspend fun get(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ) =
    lock
      .scheduleUnlock {
        super.get(
          config,
          route,
          build = {
            bearerAuth(it.accessToken)
            build()
          }
        )
      }
      .flatten()

  override suspend fun post(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ) =
    lock
      .scheduleUnlock {
        super.post(
          config,
          route,
          build = {
            bearerAuth(it.accessToken)
            build()
          }
        )
      }
      .flatten()

  override suspend fun post(
    config: Configuration,
    route: URI,
    form: List<PartData>,
    build: HttpRequestBuilder.() -> Unit
  ) =
    lock
      .scheduleUnlock {
        super.post(
          config,
          route,
          form,
          build = {
            bearerAuth(it.accessToken)
            build()
          }
        )
      }
      .flatten()
}

/**
 * Returns a [Requester] whose requests require authentication in order to be performed.
 *
 * @throws Injector.ModuleNotRegisteredException If a [CoreModule] has not been registered.
 */
@Throws(Injector.ModuleNotRegisteredException::class)
internal fun Requester<*>.authenticated() =
  authenticated(Injector.from<CoreModule>().authenticationLock())

/**
 * Returns a [Requester] whose requests require authentication in order to be performed.
 *
 * @param lock [AuthenticationLock] for unlocking requests made by an unauthenticated [Actor].
 */
internal fun Requester<*>.authenticated(
  lock: SomeAuthenticationLock
): Requester<AuthenticationLock.FailedAuthenticationException> =
  (this as? AuthenticatedRequester)?.takeIf { it.lock == lock }
    ?: AuthenticatedRequester(logger, baseURI, clientEngineFactory, lock)
