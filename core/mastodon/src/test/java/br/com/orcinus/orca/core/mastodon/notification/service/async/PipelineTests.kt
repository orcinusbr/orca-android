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

package br.com.orcinus.orca.core.mastodon.notification.service.async

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isZero
import assertk.coroutines.assertions.suspendCall
import java.util.concurrent.CompletionStage
import kotlin.test.Test
import kotlinx.coroutines.future.await
import kotlinx.coroutines.test.runTest

internal class PipelineTests {
  @Test
  fun createsWithImmediateValue() {
    runTest {
      assertThat(pipelineOf<Byte>(0)).suspendCall("await", CompletionStage<Byte>::await).isZero()
    }
  }

  @Test
  fun createsWithAsyncValue() {
    runTest {
      assertThat(pipeline<Byte> { 0 }).suspendCall("await", CompletionStage<Byte>::await).isZero()
    }
  }

  @Test
  fun composesAtopImmediateValue() {
    runTest {
      assertThat(pipelineOf<Byte>(0))
        .transform("thenApply") { it.thenApply(2::plus) }
        .suspendCall("await", Pipeline<Int>::await)
        .isEqualTo(2)
    }
  }

  @Test
  fun composesAtopAsyncValue() {
    runTest {
      assertThat(pipelineOf<Byte>(0))
        .transform("thenApply") { it.thenApply(2::plus) }
        .suspendCall("await", Pipeline<Int>::await)
        .isEqualTo(2)
    }
  }
}
