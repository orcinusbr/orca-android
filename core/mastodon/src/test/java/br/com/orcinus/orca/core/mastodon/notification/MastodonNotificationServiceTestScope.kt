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

import android.content.Intent
import br.com.orcinus.orca.core.mastodon.instance.requester.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.AuthenticatedRequester
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.platform.testing.context
import io.ktor.client.HttpClient
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
 * Default implementation of a [MastodonNotificationServiceTestScope] which will be used in the
 * tests.
 *
 * @param delegate [TestScope] to which [CoroutineScope] behavior will be delegated.
 */
private class MastodonNotificationServiceEnvironment(
  override val controller: ServiceController<MastodonNotificationService>,
  override val requester: AuthenticatedRequester,
  delegate: TestScope
) : MastodonNotificationServiceTestScope, CoroutineScope by delegate

/** Scope in which a [MastodonNotificationService] test is run. */
internal sealed interface MastodonNotificationServiceTestScope : CoroutineScope {
  /** [ServiceController] by which the lifecycle of the [MastodonNotificationService] is managed. */
  val controller: ServiceController<MastodonNotificationService>

  /** [AuthenticatedRequester] by which subscriptions are pushed. */
  val requester: AuthenticatedRequester
}

/**
 * Runs a [MastodonNotificationService]-focused test.
 *
 * @param clientResponseProvider Defines how the [HttpClient] will respond to requests.
 * @param coroutineContext [CoroutineContext] in which [Job]s are launched by default.
 * @param body Testing to be performed on a [MastodonNotificationService].
 */
@OptIn(ExperimentalContracts::class)
internal fun runMastodonNotificationServiceTest(
  clientResponseProvider: ClientResponseProvider = ClientResponseProvider.ok,
  coroutineContext: CoroutineContext = EmptyCoroutineContext,
  body: suspend MastodonNotificationServiceTestScope.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runAuthenticatedRequesterTest(clientResponseProvider, context = coroutineContext) {
    val service = MastodonNotificationService(requester, requester.lock, coroutineContext)
    val intent = Intent(context, service::class.java)
    val controller = ServiceController.of(service, intent)
    try {
      MastodonNotificationServiceEnvironment(controller, requester, delegate).body()
    } finally {
      controller.unbind().destroy()
    }
  }
}
