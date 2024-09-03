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

package br.com.orcinus.orca.platform.autos.test.kit.action.setting

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasTextExactly
import androidx.compose.ui.test.onAncestors
import br.com.orcinus.orca.platform.autos.kit.action.setting.LabelTag

/**
 * [SemanticsNodeInteraction] of a setting.
 *
 * @param label Label of the setting.
 */
fun SemanticsNodeInteractionsProvider.onSetting(label: String): SemanticsNodeInteraction {
  return onNode(hasTestTag(LabelTag) and hasTextExactly(label, includeEditableText = false))
    .onAncestors()
    .filterToOne(isSetting())
}
