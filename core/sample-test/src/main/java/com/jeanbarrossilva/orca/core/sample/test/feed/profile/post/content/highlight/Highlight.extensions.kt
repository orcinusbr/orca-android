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

package com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.content.highlight

import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.core.sample.test.image.TestSampleImageLoader

/** [Highlight] returned by [sample]. */
private val testSampleHighlight = Highlight.createSample(TestSampleImageLoader.Provider)

/** Test sample [Highlight]. */
val Highlight.Companion.sample
  get() = testSampleHighlight
