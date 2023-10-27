package com.jeanbarrossilva.orca.platform.theme.kit.input.text.error

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

internal class ErrorDispatcherTests {
  @Test
  fun doesNotAnnounceErrorsBeforeDispatch() {
    var hasBeenAnnounced = false
    val onAnnouncementListener = ErrorDispatcher.OnAnnouncementListener { hasBeenAnnounced = true }
    ErrorDispatcher().apply {
      use {
        error("⚠️") { true }
        listen(onAnnouncementListener)
        register("🐋")
      }
    }
    assertThat(hasBeenAnnounced).isFalse()
  }

  @Test
  fun dispatches() {
    val dispatcher =
      ErrorDispatcher().apply {
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
    ErrorDispatcher().use { dispatcher ->
      dispatcher.error("😷") { text -> text.length == 1 }
      dispatcher.listen(onAnnouncementListener)
      dispatcher.register("🐳")
      dispatcher.dispatch()
    }
    assertThat(hasErrorBeenAnnounced).isTrue()
  }

  @Test
  fun resetsAfterUsage() {
    val dispatcher =
      ErrorDispatcher().apply {
        use {
          error("🇳🇦") { true }
          listen {}
          register("🍕")
          dispatch()
        }
      }
    assertThat(dispatcher.hasDispatched).isFalse()
  }
}
