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

package com.jeanbarrossilva.orca.feature.gallery.ui.test

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.orca.feature.gallery.ui.Actions
import com.jeanbarrossilva.orca.feature.gallery.ui.GALLERY_ACTIONS_CLOSE_BUTTON_TAG
import com.jeanbarrossilva.orca.feature.gallery.ui.GALLERY_ACTIONS_OPTIONS_BUTTON_TAG
import com.jeanbarrossilva.orca.feature.gallery.ui.GALLERY_ACTIONS_OPTIONS_DOWNLOADS_ITEM_TAG
import com.jeanbarrossilva.orca.feature.gallery.ui.GALLERY_ACTIONS_OPTIONS_MENU_TAG
import com.jeanbarrossilva.orca.feature.gallery.ui.GALLERY_ACTIONS_TAG
import com.jeanbarrossilva.orca.feature.gallery.ui.Gallery
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.kit.menu.DropdownMenu

/** [SemanticsNodeInteraction] of a [Gallery]'s [Actions]. */
internal fun SemanticsNodeInteractionsProvider.onActions(): SemanticsNodeInteraction {
  return onNodeWithTag(GALLERY_ACTIONS_TAG)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [Actions]' close [HoverableIconButton].
 *
 * @see onActions
 */
internal fun SemanticsNodeInteractionsProvider.onCloseActionButton(): SemanticsNodeInteraction {
  return onNodeWithTag(GALLERY_ACTIONS_CLOSE_BUTTON_TAG)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [Actions]' download option [DropdownMenuItem].
 *
 * @see onActions
 * @see onOptionsButton
 */
internal fun SemanticsNodeInteractionsProvider.onDownloadItem(): SemanticsNodeInteraction {
  return onNodeWithTag(GALLERY_ACTIONS_OPTIONS_DOWNLOADS_ITEM_TAG)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [Actions]' options [HoverableIconButton].
 *
 * @see onActions
 */
internal fun SemanticsNodeInteractionsProvider.onOptionsButton(): SemanticsNodeInteraction {
  return onNodeWithTag(GALLERY_ACTIONS_OPTIONS_BUTTON_TAG)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [Actions]' options [DropdownMenu].
 *
 * @see onActions
 */
internal fun SemanticsNodeInteractionsProvider.onOptionsMenu(): SemanticsNodeInteraction {
  return onNodeWithTag(GALLERY_ACTIONS_OPTIONS_MENU_TAG)
}

/**
 * [SemanticsNodeInteraction] of a [Gallery]'s [HorizontalPager].
 *
 * @see isPager
 */
internal fun SemanticsNodeInteractionsProvider.onPager(): SemanticsNodeInteraction {
  return onNode(isPager())
}
