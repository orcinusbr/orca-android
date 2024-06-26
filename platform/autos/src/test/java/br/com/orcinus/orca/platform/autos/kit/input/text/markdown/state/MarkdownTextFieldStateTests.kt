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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.state

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.test.Test
import org.junit.Rule

internal class MarkdownTextFieldStateTests {
  @get:Rule val stateRule = MarkdownTextFieldStateTestRule()

  @Test
  fun notifiesOnStylizationListener() {
    var styles = emptyList<Style>()
    stateRule.state.setOnStylizationListener { styles = it }
    stateRule.state.toggle(Style.Bold(indices = 0..1))
    stateRule.state.toggle(Style.Italic(indices = 0..1))
    assertThat(styles).containsExactly(Style.Bold(indices = 0..1), Style.Italic(indices = 0..1))
  }

  @Test
  fun setsInitialStyles() {
    stateRule.state.setInitialStyles(listOf(Style.Bold(indices = 0..1)))
    assertThat(stateRule.state.styles).containsExactly(Style.Bold(indices = 0..1))
  }

  @Test
  fun doesNotSetStylesWhenTheyAreNotTheInitialOnes() {
    stateRule.state.toggle(Style.Bold(indices = 0..1))
    stateRule.state.setInitialStyles(listOf(Style.Italic(indices = 0..1)))
    assertThat(stateRule.state.styles).containsExactly(Style.Bold(indices = 0..1))
  }

  @Test
  fun appliesUnappliedStyleWhenTogglingIt() {
    stateRule.state.toggle(Style.Bold(indices = 0..1))
    assertThat(stateRule.state.styles).containsExactly(Style.Bold(indices = 0..1))
  }

  @Test
  fun removesAppliedStyleWhenTogglingIt() {
    repeat(2) { stateRule.state.toggle(Style.Bold(indices = 0..1)) }
    assertThat(stateRule.state.styles).isEmpty()
  }

  @Test
  fun removesOnStylizationListenerWhenResetting() {
    var listenerNotificationCount = 0
    stateRule.state.setOnStylizationListener { listenerNotificationCount++ }
    stateRule.state.reset()
    stateRule.state.toggle(Style.Bold(indices = 0..1))
    assertThat(listenerNotificationCount).isEqualTo(1)
  }

  @Test
  fun removesStylesWhenResetting() {
    stateRule.state.toggle(Style.Bold(indices = 0..1))
    stateRule.state.reset()
    assertThat(stateRule.state.styles).isEmpty()
  }
}
