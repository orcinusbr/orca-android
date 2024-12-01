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

package br.com.orcinus.orca.core.mastodon.notification

import android.content.Context
import android.content.Intent
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.AuthenticatedRequester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.platform.testing.context
import io.ktor.client.HttpClient
import java.net.URI
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import org.robolectric.android.controller.ServiceController

/** Default implementation of a [NotificationServiceTestScope] which will be used in the tests. */
private class NotificationServiceEnvironment(
  override val requester: AuthenticatedRequester,
  override val clientResponseProvider: NotificationsClientResponseProvider,
  override val context: Context,
  delegate: TestScope
) : NotificationServiceTestScope(), CoroutineScope by delegate

/** Scope in which a [NotificationService] test is run. */
internal sealed class NotificationServiceTestScope : CoroutineScope {
  /** [ServiceController] by which the lifecycle of the [NotificationService] is managed. */
  private val controller by lazy {
    NotificationService(requester, WebPush())
      .apply { setCoroutineContext(coroutineContext) }
      .let { ServiceController.of(it, Intent(context, it::class.java)) }
  }

  /** [Context] in which the [service] is instantiated. */
  protected abstract val context: Context

  /** [AuthenticatedRequester] by which subscriptions are pushed. */
  abstract val requester: AuthenticatedRequester

  /** [NotificationsClientResponseProvider] by which responses to requests are provided. */
  abstract val clientResponseProvider: NotificationsClientResponseProvider

  /** [NotificationService] being tested. */
  val service: NotificationService
    get() = controller.get()

  /**
   * Creates the [service].
   *
   * @see NotificationService.onCreate
   */
  fun create() {
    controller.create()
  }

  /**
   * Destroys the [service].
   *
   * @see NotificationService.onDestroy
   */
  fun destroy() {
    controller.destroy()
  }
}

/**
 * Runs a [NotificationService]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param coroutineContext [CoroutineContext] in which [Job]s are launched by default.
 * @param body Testing to be performed on a [NotificationService].
 */
@OptIn(ExperimentalContracts::class)
internal fun runNotificationServiceTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  onAuthentication: () -> Unit = {},
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  body: suspend NotificationServiceTestScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  lateinit var baseURI: URI

  /*
   * Responses are provided by a MastodonNotificationsClientResponseProvider, which responds to
   * requests that aim to obtain the notifications received from the Mastodon server. The specified
   * provider is used only if they do not.
   */
  val defaultClientResponseProvider =
    NotificationsClientResponseProvider({ baseURI }, next = clientResponseProvider)

  runAuthenticatedRequesterTest(defaultClientResponseProvider, onAuthentication, coroutineContext) {
    baseURI = requester.baseURI
    NotificationServiceEnvironment(requester, defaultClientResponseProvider, context, delegate)
      .run {
        try {
          body()
        } finally {
          destroy()
        }
      }
  }
}
