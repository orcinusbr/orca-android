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

package br.com.orcinus.orca.core.mastodon.instance.requester.authentication

import androidx.annotation.VisibleForTesting
import br.com.orcinus.orca.core.auth.AuthenticationLock
import br.com.orcinus.orca.core.auth.SomeAuthenticationLock
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.InternalRequesterApi
import br.com.orcinus.orca.core.mastodon.instance.requester.Logger
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.std.injector.Injector
import br.com.orcinus.orca.std.injector.module.Module
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
internal class AuthenticatedRequester
@InternalRequesterApi
constructor(
  logger: Logger,
  baseURI: URI,
  clientEngineFactory: HttpClientEngineFactory<*>,
  @get:InternalRequesterApi @get:VisibleForTesting internal val lock: SomeAuthenticationLock
) : Requester(logger, baseURI, clientEngineFactory) {
  override suspend fun delete(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return lock.scheduleUnlock {
      super.delete(
        config,
        route,
        build = {
          bearerAuth(it.accessToken)
          build()
        }
      )
    }
  }

  override suspend fun get(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return lock.scheduleUnlock {
      super.get(
        config,
        route,
        build = {
          bearerAuth(it.accessToken)
          build()
        }
      )
    }
  }

  override suspend fun post(
    config: Configuration,
    route: URI,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return lock.scheduleUnlock {
      super.post(
        config,
        route,
        build = {
          bearerAuth(it.accessToken)
          build()
        }
      )
    }
  }

  override suspend fun post(
    config: Configuration,
    route: URI,
    form: List<PartData>,
    build: HttpRequestBuilder.() -> Unit
  ): HttpResponse {
    return lock.scheduleUnlock {
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
  }
}

/**
 * Creates an [AuthenticatedRequester] based on the receiver [Requester], with the
 * [AuthenticationLock] injected into the registered [CoreModule].
 *
 * @throws Injector.ModuleNotRegisteredException If a [CoreModule] hasn't been registered.
 * @throws Module.DependencyNotInjectedException If an [AuthenticationLock] hasn't been injected
 *   into the registered [CoreModule].
 * @see CoreModule.authenticationLock
 */
@Throws(Injector.ModuleNotRegisteredException::class, Module.DependencyNotInjectedException::class)
internal fun Requester.authenticated(): AuthenticatedRequester {
  return if (this is AuthenticatedRequester) {
    this
  } else {
    val lock = Injector.from<CoreModule>().authenticationLock()
    AuthenticatedRequester(logger, baseURI, clientEngineFactory, lock)
  }
}
