/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.composite.timeline.post

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey

/** [SemanticsPropertyKey] returned by [PostPreview]. */
private val postPreviewSemanticsPropertyKey = SemanticsPropertyKey<PostPreview>("PostPreview")

/** [SemanticsPropertyKey] of a [PostPreview]. */
val @receiver:Suppress("UnusedReceiverParameter") SemanticsProperties.PostPreview
  get() = postPreviewSemanticsPropertyKey
