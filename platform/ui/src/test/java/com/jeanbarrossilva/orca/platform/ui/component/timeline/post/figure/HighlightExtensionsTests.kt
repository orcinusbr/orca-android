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

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.image.CoverImageSource
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader
import java.net.URL
import kotlin.test.Test

internal class HighlightExtensionsTests {
  @Test
  fun convertsIntoFigure() {
    val coverLoader = TestSampleImageLoader.Provider.provide(CoverImageSource.Default)
    val headline = Headline("Title", "Subtitle", coverLoader)
    val url = URL("https://orca.jeanbarrossilva.com")
    assertThat(Highlight(headline, url).toFigure()).isEqualTo(Figure(headline, url))
  }
}
