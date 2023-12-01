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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.orca.platform.autos.R
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme

/**
 * [TopAppBar] with a back button as the navigation action.
 *
 * @param onNavigation Callback run when back navigation is requested to be performed.
 * @param title Short explanation of what's being presented by the overall content.
 * @param modifier [Modifier] to be applied to the underlying [TopAppBar].
 * @param subtitle Contextualizes the [title].
 * @param actions [IconButton]s with actions to be performed in this context.
 * @param scrollBehavior Defines how this [TopAppBar] behaves on scroll.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarWithBackNavigation(
  onNavigation: () -> Unit,
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  subtitle: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.scrollBehavior
) {
  TopAppBar(
    title,
    modifier,
    navigationIcon = { BackAction(onClick = onNavigation) },
    subtitle,
    actions,
    scrollBehavior
  )
}

/**
 * Back navigation action of a [TopAppBarWithBackNavigation].
 *
 * @param onClick Callback run whenever the [HoverableIconButton] is clicked.
 * @param modifier [Modifier] to the underlying [HoverableIconButton].
 */
@Composable
fun BackAction(onClick: () -> Unit, modifier: Modifier = Modifier) {
  HoverableIconButton(onClick, modifier) {
    Icon(
      AutosTheme.iconography.back.asImageVector,
      contentDescription =
        stringResource(R.string.platform_ui_top_app_bar_with_back_navigation_action)
    )
  }
}
