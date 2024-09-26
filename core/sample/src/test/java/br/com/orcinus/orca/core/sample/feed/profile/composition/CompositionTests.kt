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

package br.com.orcinus.orca.core.sample.feed.profile.composition

import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.sample.feed.profile.composition.Composers.compose
import java.time.ZonedDateTime
import kotlin.test.Test

internal class CompositionTests {
  @Test
  fun createsATimedComposition() {
    val composer = HardcodedComposer()
    val content = Content.empty
    val dateTime = ZonedDateTime.now()
    assertThat(composer.compose(content).on(dateTime))
      .isEqualTo(TimedComposition(composer, content, dateTime))
  }
}
