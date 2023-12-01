/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.auth.actor

import com.jeanbarrossilva.orca.core.test.TestActorProvider
import com.jeanbarrossilva.orca.core.test.TestAuthenticator
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class ActorProviderTests {
  @Test
  fun `GIVEN a provider WHEN authenticating THEN it provides the resulting actor`() {
    val actorProvider = TestActorProvider()
    val authenticator = TestAuthenticator(actorProvider = actorProvider)
    runTest {
      val actor = authenticator.authenticate()
      assertEquals(actor, actorProvider.provide())
    }
  }
}
