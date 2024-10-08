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
import br.com.orcinus.orca.core.mastodon.instance.requester.authentication.runAuthenticatedRequesterTest
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.module.authenticationLock
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.injector.Injector
import io.ktor.client.HttpClient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.Job
import org.robolectric.android.controller.ServiceController

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
  body: suspend ServiceController<MastodonNotificationService>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runAuthenticatedRequesterTest(clientResponseProvider, context = coroutineContext) {
    val authenticationLock = Injector.from<CoreModule>().authenticationLock()
    val service = MastodonNotificationService(requester, authenticationLock, coroutineContext)
    val intent = Intent(context, service::class.java)
    val controller = ServiceController.of(service, intent)
    try {
      controller.body()
    } finally {
      controller.unbind().destroy()
    }
  }
}
