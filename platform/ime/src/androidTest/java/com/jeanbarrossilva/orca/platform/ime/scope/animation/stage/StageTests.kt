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

package com.jeanbarrossilva.orca.platform.ime.scope.animation.stage

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isSameAs
import assertk.assertions.isTrue
import java.time.Duration
import kotlin.test.Test

internal class StageTests {
  @Test
  fun idleIsIdle() {
    assertThat(Stage.idle().isIdle).isTrue()
  }

  @Test
  fun idleIsNotPrepared() {
    assertThat(Stage.idle().isPrepared).isFalse()
  }

  @Test
  fun idleIsNotStarted() {
    assertThat(Stage.idle().isStarted).isFalse()
  }

  @Test
  fun idleIsNotOngoing() {
    assertThat(Stage.idle().isOngoing).isFalse()
  }

  @Test
  fun idleIsNotEnded() {
    assertThat(Stage.idle().isEnded).isFalse()
  }

  @Test
  fun idlePlusIdleEqualsIdle() {
    assertThat(Stage.idle() + Stage.idle()).isSameAs(Stage.idle())
  }

  @Test
  fun idlePlusPreparedEqualsPrepared() {
    val prepared = Stage.prepared(Duration.ofMillis(2))
    assertThat(Stage.idle() + prepared).isEqualTo(prepared)
  }

  @Test
  fun idlePlusStartedEqualsStarted() {
    val started = Stage.started(Duration.ZERO)
    assertThat(Stage.idle() + started).isEqualTo(started)
  }

  @Test
  fun idlePlusOngoingEqualsOngoing() {
    val ongoing = Stage.ongoing(Duration.ZERO)
    assertThat(Stage.idle() + ongoing).isEqualTo(ongoing)
  }

  @Test
  fun idlePlusEndedEqualsEnded() {
    assertThat(Stage.idle() + Stage.ended()).isEnded()
  }

  @Test
  fun preparedIsNotIdle() {
    assertThat(Stage.prepared(Duration.ZERO).isIdle).isFalse()
  }

  @Test
  fun preparedIsPrepared() {
    assertThat(Stage.prepared(Duration.ZERO).isPrepared).isTrue()
  }

  @Test
  fun preparedIsNotStarted() {
    assertThat(Stage.prepared(Duration.ZERO).isStarted).isFalse()
  }

  @Test
  fun preparedIsNotOngoing() {
    assertThat(Stage.prepared(Duration.ZERO).isOngoing).isFalse()
  }

  @Test
  fun preparedIsNotEnded() {
    assertThat(Stage.prepared(Duration.ZERO).isEnded).isFalse()
  }

  @Test
  fun preparedPlusIdleEqualsIdle() {
    assertThat(Stage.prepared(Duration.ZERO) + Stage.idle()).isSameAs(Stage.idle())
  }

  @Test
  fun preparedPlusPreparedEqualsPreparedWithSummedDuration() {
    val prepared = Stage.prepared(Duration.ofMillis(2))
    assertThat(prepared + prepared).isEqualTo(Stage.prepared(Duration.ofMillis(4)))
  }

  @Test
  fun preparedPlusStartedEqualsStartedWithSummedDuration() {
    assertThat(Stage.prepared(Duration.ofMillis(2)) + Stage.started(Duration.ofMillis(2)))
      .isEqualTo(Stage.started(Duration.ofMillis(4)))
  }

  @Test
  fun preparedPlusOngoingEqualsOngoingWithSummedDuration() {
    assertThat(Stage.prepared(Duration.ofMillis(2)) + Stage.ongoing(Duration.ofMillis(2)))
      .isEqualTo(Stage.ongoing(Duration.ofMillis(4)))
  }

  @Test
  fun preparedPlusEndedEqualsEnded() {
    assertThat(Stage.prepared(Duration.ofMillis(2)) + Stage.ended()).isEnded()
  }

  @Test
  fun startedIsNotIdle() {
    assertThat(Stage.started(Duration.ZERO).isIdle).isFalse()
  }

  @Test
  fun startedIsNotPrepared() {
    assertThat(Stage.started(Duration.ZERO).isPrepared).isFalse()
  }

  @Test
  fun startedIsStarted() {
    assertThat(Stage.started(Duration.ZERO).isStarted).isTrue()
  }

  @Test
  fun startedIsNotOngoing() {
    assertThat(Stage.started(Duration.ZERO).isOngoing).isFalse()
  }

  @Test
  fun startedIsNotEnded() {
    assertThat(Stage.started(Duration.ZERO).isEnded).isFalse()
  }

  @Test
  fun startedPlusIdleEqualsIdle() {
    assertThat(Stage.started(Duration.ZERO) + Stage.idle()).isSameAs(Stage.idle())
  }

  @Test
  fun startedPlusPreparedEqualsPreparedWithSummedDuration() {
    assertThat(Stage.started(Duration.ofMillis(2)) + Stage.prepared(Duration.ofMillis(2)))
      .isEqualTo(Stage.prepared(Duration.ofMillis(4)))
  }

  @Test
  fun startedPlusStartedEqualsStartedWithSummedDuration() {
    val started = Stage.started(Duration.ofMillis(2))
    assertThat(started + started).isEqualTo(Stage.started(Duration.ofMillis(4)))
  }

  @Test
  fun startedPlusOngoingEqualsOngoingWithSummedDuration() {
    assertThat(Stage.started(Duration.ofMillis(2)) + Stage.ongoing(Duration.ofMillis(2)))
      .isEqualTo(Stage.ongoing(Duration.ofMillis(4)))
  }

  @Test
  fun startedPlusEndedEqualsEnded() {
    assertThat(Stage.started(Duration.ofMillis(2)) + Stage.ended()).isEnded()
  }

  @Test
  fun ongoingIsNotIdle() {
    assertThat(Stage.ongoing(Duration.ZERO).isIdle).isFalse()
  }

  @Test
  fun ongoingIsNotPrepared() {
    assertThat(Stage.ongoing(Duration.ZERO).isPrepared).isFalse()
  }

  @Test
  fun ongoingIsNotStarted() {
    assertThat(Stage.ongoing(Duration.ZERO).isStarted).isFalse()
  }

  @Test
  fun ongoingIsOngoing() {
    assertThat(Stage.ongoing(Duration.ZERO).isOngoing).isTrue()
  }

  @Test
  fun ongoingIsNotEnded() {
    assertThat(Stage.ongoing(Duration.ZERO).isEnded).isFalse()
  }

  @Test
  fun ongoingPlusIdleEqualsIdle() {
    assertThat(Stage.ongoing(Duration.ZERO) + Stage.idle()).isSameAs(Stage.idle())
  }

  @Test
  fun ongoingPlusPreparedEqualsPreparedWithSummedDuration() {
    assertThat(Stage.ongoing(Duration.ofMillis(2)) + Stage.prepared(Duration.ofMillis(2)))
      .isEqualTo(Stage.prepared(Duration.ofMillis(4)))
  }

  @Test
  fun ongoingPlusStartedEqualsStartedWithSummedDuration() {
    assertThat(Stage.ongoing(Duration.ofMillis(2)) + Stage.started(Duration.ofMillis(2)))
      .isEqualTo(Stage.started(Duration.ofMillis(4)))
  }

  @Test
  fun ongoingPlusOngoingEqualsOngoingWithSummedDuration() {
    assertThat(Stage.ongoing(Duration.ofMillis(2)) + Stage.ongoing(Duration.ofMillis(2)))
      .isEqualTo(Stage.ongoing(Duration.ofMillis(4)))
  }

  @Test
  fun ongoingPlusEndedEqualsEnded() {
    assertThat(Stage.ongoing(Duration.ofMillis(2)) + Stage.ended()).isEnded()
  }

  @Test
  fun endedIsNotIdle() {
    assertThat(Stage.ended().isIdle).isFalse()
  }

  @Test
  fun endedIsNotPrepared() {
    assertThat(Stage.ended().isPrepared).isFalse()
  }

  @Test
  fun endedIsNotStarted() {
    assertThat(Stage.ended().isStarted).isFalse()
  }

  @Test
  fun endedIsNotOngoing() {
    assertThat(Stage.ended().isOngoing).isFalse()
  }

  @Test
  fun endedIsEnded() {
    assertThat(Stage.ended()).isEnded()
  }

  @Test
  fun endedPlusIdleEqualsIdle() {
    assertThat(Stage.ended() + Stage.idle()).isSameAs(Stage.idle())
  }

  @Test
  fun endedPlusPreparedEqualsPrepared() {
    assertThat(Stage.ended() + Stage.prepared(Duration.ofMillis(2)))
      .isEqualTo(Stage.prepared(Duration.ofMillis(2)))
  }

  @Test
  fun endedPlusStartedEqualsStarted() {
    assertThat(Stage.ended() + Stage.started(Duration.ofMillis(2)))
      .isEqualTo(Stage.started(Duration.ofMillis(2)))
  }

  @Test
  fun endedPlusOngoingEqualsOngoing() {
    assertThat(Stage.ended() + Stage.ongoing(Duration.ofMillis(2)))
      .isEqualTo(Stage.ongoing(Duration.ofMillis(2)))
  }

  @Test
  fun endedPlusEndedEqualsEnded() {
    assertThat(Stage.ended() + Stage.ended()).isEnded()
  }
}
