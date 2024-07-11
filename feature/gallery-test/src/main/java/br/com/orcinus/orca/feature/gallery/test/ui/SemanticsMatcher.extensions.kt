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

package br.com.orcinus.orca.feature.gallery.test.ui

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasTestTag
import br.com.orcinus.orca.feature.gallery.ui.Gallery
import br.com.orcinus.orca.feature.gallery.ui.GalleryPagerTag

/** [SemanticsMatcher] that matches a [Gallery]'s [HorizontalPager]. */
fun isPager(): SemanticsMatcher {
  return hasTestTag(GalleryPagerTag)
}

/**
 * [SemanticsMatcher] that matches an image.
 *
 * @see Role.Image
 */
internal fun isImage(): SemanticsMatcher {
  return SemanticsMatcher("is image") {
    it.config.getOrNull(SemanticsProperties.Role) == Role.Image
  }
}
