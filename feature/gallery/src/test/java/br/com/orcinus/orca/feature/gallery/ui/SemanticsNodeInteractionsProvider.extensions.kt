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

package br.com.orcinus.orca.feature.gallery.ui

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithTag
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.menu.DropdownMenu

/** [SemanticsNodeInteraction] of a [SampleGallery]'s [Actions]. */
internal fun SemanticsNodeInteractionsProvider.onActions(): SemanticsNodeInteraction {
  return onNodeWithTag(GalleryActionsTag)
}

/**
 * [SemanticsNodeInteraction] of a [SampleGallery]'s [Actions]' download option [DropdownMenuItem].
 *
 * @see onActions
 * @see onOptionsButton
 */
internal fun SemanticsNodeInteractionsProvider.onDownloadItem(): SemanticsNodeInteraction {
  return onNodeWithTag(GalleryActionsOptionsDownloadItemTag)
}

/**
 * [SemanticsNodeInteraction] of a [SampleGallery]'s [Actions]' options [HoverableIconButton].
 *
 * @see onActions
 */
internal fun SemanticsNodeInteractionsProvider.onOptionsButton(): SemanticsNodeInteraction {
  return onNodeWithTag(GalleryActionsOptionsButtonTag)
}

/**
 * [SemanticsNodeInteraction] of a [SampleGallery]'s [Actions]' options [DropdownMenu].
 *
 * @see onActions
 */
internal fun SemanticsNodeInteractionsProvider.onOptionsMenu(): SemanticsNodeInteraction {
  return onNodeWithTag(GalleryActionsOptionsMenuTag)
}
