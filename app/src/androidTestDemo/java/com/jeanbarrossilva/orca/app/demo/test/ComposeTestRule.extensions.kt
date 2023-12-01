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

package com.jeanbarrossilva.orca.app.demo.test

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTouchInput
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.headline.HeadlineCard
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import kotlin.properties.Delegates
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

/** Scrolls to the first [PostPreview] containing a [HeadlineCard]. */
internal fun ComposeTestRule.performScrollToPostPreviewWithHeadlineCard() {
  var index by Delegates.notNull<Int>()
  runTest {
    index =
      Instance.sample.feedProvider
        .provide(Actor.Authenticated.sample.id, page = 0)
        .first()
        .withIndex()
        .first { it.value.content.highlight != null }
        .index
  }
  onTimeline().performScrollToIndex(index)
}

/** Performs a click on the portion located at [Offset.Zero] of this [SemanticsNode]. */
internal fun SemanticsNodeInteraction.performStartClick() {
  performTouchInput { click(Offset.Zero) }
}
