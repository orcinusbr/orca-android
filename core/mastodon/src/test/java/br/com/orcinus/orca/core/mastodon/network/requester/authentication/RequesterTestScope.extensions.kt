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

package br.com.orcinus.orca.core.mastodon.network.requester.authentication

import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.mastodon.network.requester.Requester
import br.com.orcinus.orca.core.mastodon.network.requester.RequesterTestScope
import br.com.orcinus.orca.core.mastodon.network.requester.runRequesterTest
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Runs an [AuthenticatedRequester]-focused test.
 *
 * @param onAuthentication Action run whenever the [Actor] is authenticated.
 * @param body Operation to be performed with the [Requester].
 */
@OptIn(ExperimentalContracts::class)
internal inline fun runAuthenticatedRequesterTest(
  crossinline onAuthentication: () -> Unit = {},
  crossinline body: suspend RequesterTestScope<AuthenticatedRequester>.() -> Unit
) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  runRequesterTest(onAuthentication = onAuthentication) {
    val authenticatedRequester = requester.authenticated()
    RequesterTestScope(delegate, authenticatedRequester, route).body()
  }
}
