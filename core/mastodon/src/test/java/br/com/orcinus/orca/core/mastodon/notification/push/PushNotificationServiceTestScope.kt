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

package br.com.orcinus.orca.core.mastodon.notification.push

import android.content.Context
import android.content.Intent
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.mastodon.notification.push.web.WebPushClient
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

/**
 * Default implementation of a [PushNotificationServiceTestScope] which will be used in the tests.
 */
private class PushNotificationServiceEnvironment(
  override val requester: Requester,
  override val clientResponseProvider: NotificationsClientResponseProvider,
  override val context: Context,
  delegate: TestScope
) : PushNotificationServiceTestScope(), CoroutineScope by delegate

/** Scope in which a [PushNotificationService] test is run. */
internal sealed class PushNotificationServiceTestScope : CoroutineScope {
  /** [ServiceController] by which the lifecycle of the [PushNotificationService] is managed. */
  private val controller by lazy {
    PushNotificationService(requester, WebPushClient())
      .apply { setCoroutineContext(coroutineContext) }
      .let { ServiceController.of(it, Intent(context, it::class.java)) }
  }

  /** [Context] in which the [service] is instantiated. */
  protected abstract val context: Context

  /** [Requester] by which subscriptions are pushed. */
  abstract val requester: Requester

  /** [NotificationsClientResponseProvider] by which responses to requests are provided. */
  abstract val clientResponseProvider: NotificationsClientResponseProvider

  /** [PushNotificationService] being tested. */
  val service: PushNotificationService
    get() = controller.get()

  /**
   * Creates the [service].
   *
   * @see PushNotificationService.onCreate
   */
  fun create() {
    controller.create()
  }

  /**
   * Destroys the [service].
   *
   * @see PushNotificationService.onDestroy
   */
  fun destroy() {
    controller.destroy()
  }
}

/**
 * Runs a [PushNotificationService]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param coroutineContext [CoroutineContext] in which [Job]s are launched by default.
 * @param body Testing to be performed on a [PushNotificationService].
 */
@OptIn(ExperimentalContracts::class)
internal fun runPushNotificationServiceTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  body: suspend PushNotificationServiceTestScope.() -> Unit
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

  runAuthenticatedRequesterTest(defaultClientResponseProvider, coroutineContext) {
    baseURI = requester.baseURI
    PushNotificationServiceEnvironment(requester, defaultClientResponseProvider, context, delegate)
      .run {
        try {
          body()
        } finally {
          destroy()
        }
      }
  }
}
