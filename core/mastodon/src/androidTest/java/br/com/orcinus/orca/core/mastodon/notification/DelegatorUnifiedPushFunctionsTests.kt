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

import br.com.orcinus.orca.platform.testing.context
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.unifiedpush.android.connector.UnifiedPush

internal class DelegatorUnifiedPushFunctionsTests {
  private val functions = DelegatorUnifiedPushFunctions(context)

  @BeforeTest fun setUp() = mockkStatic(UnifiedPush::class)

  @Test
  fun delegatesGetAckDistributor() {
    functions.getAckDistributor()
    verify(exactly = 1) { UnifiedPush.getAckDistributor(context) }
  }

  @Test
  fun delegatesGetDistributors() {
    functions.getDistributors()
    verify(exactly = 1) { UnifiedPush.getDistributors(context) }
  }

  @Test
  fun delegatesRegisterApp() {
    functions.registerApp(instance = "orca")
    verify(exactly = 1) { UnifiedPush.registerApp(context, instance = "orca") }
  }

  @Test
  fun delegatesSaveDistributor() {
    functions.saveDistributor("orca")
    verify(exactly = 1) { UnifiedPush.saveDistributor(context, "orca") }
  }

  @Test
  fun delegatesTryUseDefaultDistributor() {
    val callback = { _: Boolean -> }
    functions.tryUseDefaultDistributor(callback)
    verify(exactly = 1) { UnifiedPush.tryUseDefaultDistributor(context, callback) }
  }

  @AfterTest
  fun tearDown() {
    UnifiedPush.forceRemoveDistributor(context)
    unmockkStatic(UnifiedPush::class)
  }
}
