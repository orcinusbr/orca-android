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

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.link.LinkCard

/** Interactive portion of a [PostPreview]. */
@Immutable
sealed class Figure {
  /**
   * [Figure] that displays the data provided by the [headline].
   *
   * @param headline [Headline] with the main information about a mentioned link.
   * @param onClick Lambda to be run whenever the [Content] gets clicked.
   */
  internal data class Link(private val headline: Headline, private val onClick: () -> Unit) :
    Figure() {
    @Composable
    override fun Content(modifier: Modifier) {
      LinkCard(headline, onClick, modifier)
    }
  }

  /**
   * UI content to displayed by this [Figure].
   *
   * @param modifier [Modifier] to be applied to the underlying [Composable].
   */
  @Composable internal abstract fun Content(modifier: Modifier)

  /** UI content to displayed by this [Figure]. */
  @Composable
  internal fun Content() {
    Content(Modifier)
  }
}
