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

package br.com.orcinus.orca.core.mastodon.auth.authentication

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.auth.actor.Actor
import br.com.orcinus.orca.core.sample.auth.SampleAuthorizer
import br.com.orcinus.orca.core.test.auth.actor.InMemoryActorProvider
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.testing.context
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonAuthenticatorTests {
  @Test
  fun notifiesListenerWhenActorIsReceived() {
    var isNotified = false
    MastodonAuthenticator(context, SampleAuthorizer, InMemoryActorProvider())
      .apply { addOnActorReceiptListener { isNotified = true } }
      .receive(Actor.Authenticated.sample)
    assertThat(isNotified).isTrue()
  }

  @Test
  fun doesNotNotifyListenerAddedAfterActorReceipt() {
    var isNotified = false
    MastodonAuthenticator(context, SampleAuthorizer, InMemoryActorProvider())
      .apply {
        runTest(@OptIn(ExperimentalCoroutinesApi::class) UnconfinedTestDispatcher()) {
          launch { authenticate() }
          launch { receive(Actor.Authenticated.sample) }
        }
      }
      .also { it.addOnActorReceiptListener { isNotified = true } }
      .receive(Actor.Authenticated.sample)
    assertThat(isNotified).isFalse()
  }

  @Test
  fun doesNotNotifyRemovedListenerWhenActorIsReceived() {
    var isNotified = false
    val listener = { _: Actor -> isNotified = true }
    MastodonAuthenticator(context, SampleAuthorizer, InMemoryActorProvider())
      .apply {
        addOnActorReceiptListener(listener)
        removeOnActorReceiptListener(listener)
      }
      .receive(Actor.Authenticated.sample)
    assertThat(isNotified).isFalse()
  }
}
