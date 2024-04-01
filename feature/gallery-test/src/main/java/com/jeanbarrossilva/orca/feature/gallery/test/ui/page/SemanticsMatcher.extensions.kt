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

package com.jeanbarrossilva.orca.feature.gallery.test.ui.page

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasParent
import com.jeanbarrossilva.orca.feature.gallery.test.ui.isImage
import com.jeanbarrossilva.orca.feature.gallery.test.ui.isPager
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.platform.autos.test.isDisplayed

/**
 * [SemanticsMatcher] that matches a [Gallery]'s [HorizontalPager]'s current page.
 *
 * @see isPager
 */
fun isPage(): SemanticsMatcher {
  return hasParent(isPager()) and isDisplayed() and isImage()
}
