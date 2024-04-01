/*
 * Copyright © 2023-2024 Orca
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

package br.com.orcinus.orca.composite.timeline.post.figure.gallery.disposition

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.SemanticsPropertyKey
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.GalleryPreview
import br.com.orcinus.orca.composite.timeline.post.figure.gallery.thumbnail.Thumbnail

/** [SemanticsPropertyKey] returned by [AspectRatio]. */
private val aspectRatioSemanticsPropertyKey = SemanticsPropertyKey<Float>(name = "AspectRatio")

/** [SemanticsPropertyKey] returned by [Disposition]. */
private val dispositionSemanticsPropertyKey =
  SemanticsPropertyKey<Disposition>(name = "Disposition")

/** [SemanticsPropertyKey] of a [GalleryPreview]'s [Thumbnail]'s aspect ratio. */
internal val @Suppress("UnusedReceiverParameter") SemanticsProperties.AspectRatio
  get() = aspectRatioSemanticsPropertyKey

/** [SemanticsPropertyKey] for the form by which a [GalleryPreview]'s [Thumbnail]s are laid out. */
internal val @Suppress("UnusedReceiverParameter") SemanticsProperties.Disposition
  get() = dispositionSemanticsPropertyKey
