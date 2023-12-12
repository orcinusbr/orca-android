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

package com.jeanbarrossilva.orca.feature.gallery.test

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.feature.gallery.GALLERY_CLOSE_BUTTON
import com.jeanbarrossilva.orca.feature.gallery.Gallery
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton

/** [SemanticsNodeInteraction] of a [Gallery]'s close [HoverableIconButton]. */
internal fun SemanticsNodeInteractionsProvider.onCloseButton(): SemanticsNodeInteraction {
  return onNodeWithTag(GALLERY_CLOSE_BUTTON)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [HorizontalPager].
 *
 * @see isPager
 */
internal fun SemanticsNodeInteractionsProvider.onPager(): SemanticsNodeInteraction {
  return onNode(isPager())
}
