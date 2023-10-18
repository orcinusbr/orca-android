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
import com.jeanbarrossilva.orca.core.sample.instance.sample
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.headline.HeadlineCard
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import kotlin.properties.Delegates
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

/** Scrolls to the first [TootPreview] containing a [HeadlineCard]. */
internal fun ComposeTestRule.performScrollToTootPreviewWithHeadlineCard() {
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
