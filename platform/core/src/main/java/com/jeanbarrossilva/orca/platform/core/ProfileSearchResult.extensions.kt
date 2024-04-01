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

package com.jeanbarrossilva.orca.platform.core

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.sample.feed.profile.search.createSample
import com.jeanbarrossilva.orca.platform.core.image.sample
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader

/** [ProfileSearchResult] returned by [sample]. */
private val sampleProfileSearchResult =
  ProfileSearchResult.createSample(ComposableImageLoader.Provider.sample)

/** Sample [ProfileSearchResult] whose avatar is loaded by a sample [ComposableImageLoader]. */
val ProfileSearchResult.Companion.sample
  get() = sampleProfileSearchResult
