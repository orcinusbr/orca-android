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

package com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.test

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.hasProgressBarRangeInfo
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.skeleton.ButtonScope

/**
 * [SemanticsNodeInteraction] of a [ButtonScope]'s [Content][ButtonScope.Loadable]'s
 * [CircularProgressIndicator].
 */
internal fun SemanticsNodeInteractionsProvider.onLoadingIndicator(): SemanticsNodeInteraction {
  return onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
}
