/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.input.text.error

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

internal class ErrorDispatcherTests {
  @Test
  fun doesNotAnnounceErrorsBeforeDispatch() {
    var hasBeenAnnounced = false
    val onAnnouncementListener = ErrorDispatcher.OnAnnouncementListener { hasBeenAnnounced = true }
    buildErrorDispatcher { errorAlways("⚠️") }
      .apply {
        use {
          listen(onAnnouncementListener)
          register("🐋")
        }
      }
    assertThat(hasBeenAnnounced).isFalse()
  }

  @Test
  fun dispatches() {
    val dispatcher =
      buildErrorDispatcher().apply {
        register("🍨")
        dispatch()
      }
    assertThat(dispatcher.hasDispatched).isTrue()
  }

  @Test
  fun announcesEncounteredErrorsWhenDispatched() {
    var hasErrorBeenAnnounced = false
    val onAnnouncementListener =
      ErrorDispatcher.OnAnnouncementListener { hasErrorBeenAnnounced = true }
    buildErrorDispatcher { errorAlways("😷") }
      .use { dispatcher ->
        dispatcher.listen(onAnnouncementListener)
        dispatcher.register("🐳")
        dispatcher.dispatch()
      }
    assertThat(hasErrorBeenAnnounced).isTrue()
  }

  @Test
  fun resetsAfterUsage() {
    val dispatcher =
      buildErrorDispatcher { errorAlways("🇳🇦") }
        .apply {
          use {
            listen {}
            register("🍕")
            dispatch()
          }
        }
    assertThat(dispatcher.hasDispatched).isFalse()
  }
}
