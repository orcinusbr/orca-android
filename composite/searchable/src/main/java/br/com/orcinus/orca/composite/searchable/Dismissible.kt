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

package br.com.orcinus.orca.composite.searchable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/** Tag that identifies a [Dismissible]'s "dismiss" button for testing purposes. */
const val DismissButtonTag = "dismiss-button"

/**
 * Layout with a "dismiss" button.
 *
 * @param onDismissal Callback called when dismissal is requested.
 * @param modifier [Modifier] to be applied to the underlying [ConstraintLayout].
 * @param content Content that can be dismissed.
 */
@Composable
internal fun Dismissible(
  onDismissal: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable ConstraintLayoutScope.(anchor: ConstrainedLayoutReference) -> Unit
) {
  ConstraintLayout(modifier) {
    val (contentRef, dismissButtonRef) = createRefs()

    content(contentRef)

    HoverableIconButton(
      onClick = onDismissal,
      Modifier.constrainAs(dismissButtonRef) {
          centerVerticallyTo(contentRef)
          end.linkTo(contentRef.end)
        }
        .testTag(DismissButtonTag)
    ) {
      Icon(
        AutosTheme.iconography.close.asImageVector,
        contentDescription = stringResource(R.string.composite_searchable_dismiss)
      )
    }
  }
}

@Composable
@MultiThemePreview
private fun DismissiblePreview() {
  AutosTheme {
    ConstraintLayout {
      Dismissible(onDismissal = {}) {
        @OptIn(ExperimentalMaterial3Api::class)
        TopAppBar(title = { AutoSizeText("Content") }, Modifier.constrainAs(it) {})
      }
    }
  }
}
