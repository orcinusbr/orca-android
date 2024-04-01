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

package com.jeanbarrossilva.orca.platform.autos.kit.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import com.jeanbarrossilva.orca.platform.autos.colors.LocalContainerColor
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold as _Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.navigation.NavigationBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.orcaVisuals
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.presenter.SnackbarPresenter
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.scope.Content
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.scope.ScaffoldScope
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.LocalWindowInsets
import com.jeanbarrossilva.orca.platform.autos.kit.sheet.takeOrElse
import com.jeanbarrossilva.orca.platform.autos.overlays.asPaddingValues
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * Orca-specific [Scaffold].
 *
 * @param modifier [Modifier] to be applied to the underlying [Scaffold].
 * @param topAppBar [TopAppBar] to be placed at the top.
 * @param floatingActionButton [FloatingActionButton] to be placed at the bottom, above the [bottom]
 *   and below the [SnackbarHost], horizontally centered.
 * @param floatingActionButtonPosition [FabPosition] that determines where the
 *   [floatingActionButton] will be placed.
 * @param snackbarPresenter [SnackbarPresenter] through which [Snackbar]s can be presented.
 * @param bottom [Composable] to be placed at the utmost bottom, such as a [NavigationBar] and/or a
 *   [ButtonBar].
 * @param content Main content of the current context.
 * @see ScaffoldScope.expanded
 * @see ScaffoldScope.navigable
 */
@Composable
fun Scaffold(
  modifier: Modifier = Modifier,
  topAppBar: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  floatingActionButtonPosition: FabPosition = FabPosition.End,
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter(),
  bottom: @Composable () -> Unit = {},
  content: ScaffoldScope.() -> Content
) {
  Scaffold(
    modifier,
    topAppBar,
    bottomBar = bottom,
    snackbarHost = {
      SnackbarHost(snackbarPresenter.hostState) {
        Snackbar(
          it,
          shape = AutosTheme.forms.medium.asShape,
          containerColor = it.orcaVisuals.containerColor,
          dismissActionContentColor = it.orcaVisuals.contentColor
        )
      }
    },
    floatingActionButton,
    floatingActionButtonPosition,
    LocalContainerColor.current.takeOrElse { AutosTheme.colors.background.container.asColor },
    contentWindowInsets =
      LocalWindowInsets.current.takeOrElse { ScaffoldDefaults.contentWindowInsets }
  ) {
    remember(::ScaffoldScope).content().ClippedValue(it)
  }
}

/** Preview of a [Scaffold][_Scaffold]. */
@Composable
@MultiThemePreview
private fun ScaffoldPreview() {
  val lazyListState = rememberLazyListState()
  val snackbarPresenter = rememberSnackbarPresenter()

  LaunchedEffect(snackbarPresenter) {
    snackbarPresenter.presentInfo("Info")
    snackbarPresenter.presentError("Error") {}
  }

  AutosTheme {
    _Scaffold(
      topAppBar = {
        @OptIn(ExperimentalMaterial3Api::class) TopAppBar(title = { AutoSizeText("Scaffold") })
      },
      floatingActionButton = {
        FloatingActionButton(onClick = {}) {
          Icon(AutosTheme.iconography.compose.filled.asImageVector, contentDescription = "Compose")
        }
      },
      snackbarPresenter = snackbarPresenter,
      bottom = { ButtonBar(lazyListState) }
    ) {
      expanded {
        LazyColumn(
          Modifier.fillMaxSize(),
          state = lazyListState,
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
          contentPadding = it + AutosTheme.overlays.fab.asPaddingValues
        ) {
          item { Text("Content", style = AutosTheme.typography.bodyMedium) }
        }
      }
    }
  }
}
