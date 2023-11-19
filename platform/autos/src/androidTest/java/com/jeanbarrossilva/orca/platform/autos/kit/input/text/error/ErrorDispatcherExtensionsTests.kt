package com.jeanbarrossilva.orca.platform.autos.kit.input.text.error

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.test.junit4.createComposeRule
import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.Rule
import org.junit.Test

internal class ErrorDispatcherExtensionsTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun addsMessagesOnErrorAnnouncements() {
    var messages = emptyList<String>()
    composeRule.setContent {
      val dispatcher = rememberErrorDispatcher { errorAlways("🦭") }

      with(dispatcher.messages) messages@{
        DisposableEffect(this) {
          messages = this@messages
          onDispose {}
        }
      }

      DisposableEffect(Unit) {
        dispatcher.dispatch()
        onDispose {}
      }
    }
    assertThat(messages).containsExactly("🦭")
  }
}
