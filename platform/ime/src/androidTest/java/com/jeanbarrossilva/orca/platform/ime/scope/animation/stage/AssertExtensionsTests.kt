/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.ime.scope.animation.stage

import assertk.assertThat
import java.time.Duration
import kotlin.test.Test
import org.opentest4j.AssertionFailedError

internal class AssertExtensionsTests {
  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatIdleIsEnded() {
    assertThat(Stage.idle()).isEnded()
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatStartedIsEnded() {
    assertThat(Stage.started(Duration.ZERO)).isEnded()
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenAssertingThatOngoingIsEnded() {
    assertThat(Stage.ongoing(Duration.ZERO)).isEnded()
  }

  @Test
  fun passesWhenAssertingThatEndedIsEnded() {
    assertThat(Stage.ended()).isEnded()
  }
}
