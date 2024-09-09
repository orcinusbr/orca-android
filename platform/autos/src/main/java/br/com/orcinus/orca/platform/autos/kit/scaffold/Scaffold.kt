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

package br.com.orcinus.orca.platform.autos.kit.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.testTag
import br.com.orcinus.orca.platform.autos.colors.LocalContainerColor
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.forms.asShape
import br.com.orcinus.orca.platform.autos.iconography.asImageVector
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.SnackbarTag
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.orcaVisuals
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.SnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import br.com.orcinus.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import br.com.orcinus.orca.platform.autos.kit.scaffold.scope.Content
import br.com.orcinus.orca.platform.autos.kit.scaffold.scope.ScaffoldScope
import br.com.orcinus.orca.platform.autos.kit.sheet.LocalWindowInsets
import br.com.orcinus.orca.platform.autos.kit.sheet.Zero
import br.com.orcinus.orca.platform.autos.kit.sheet.takeOrElse
import br.com.orcinus.orca.platform.autos.overlays.asPaddingValues
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview

/**
 * Orca-specific scaffold.
 *
 * @param modifier [Modifier] to be applied to the underlying [Scaffold].
 * @param topAppBar [TopAppBar] to be placed at the top.
 * @param floatingActionButton [FloatingActionButton] to be placed at the bottom, above the [bottom]
 *   and below the [SnackbarHost], horizontally centered.
 * @param floatingActionButtonPosition [FabPosition] that determines where the
 *   [floatingActionButton] will be placed.
 * @param snackbarPresenter [SnackbarPresenter] through which [Snackbar]s can be presented.
 * @param bottom [Composable] to be placed at the utmost bottom, such as a [ButtonBar].
 * @param content Main content of the current context.
 * @see ScaffoldScope.expanded
 * @see ScaffoldScope.navigable
 */
@Composable
@Deprecated(
  "Masking applied to the content, that was previously defined by whether it was \"expanded\" " +
    "or \"navigable\", is now determined by the view by which this composable is shown; thus, " +
    "specifying one of the two is redundant and a no-op. Prefer an unscoped scaffold instead.",
  ReplaceWith(
    "UnscopedScaffold(modifier, topAppBar, floatingActionButton, floatingActionButtonPosition, " +
      "snackbarPresenter, bottom, content)",
    "br.com.orcinus.orca.platform.autos.kit.scaffold.UnscopedScaffold"
  )
)
fun Scaffold(
  modifier: Modifier = Modifier,
  topAppBar: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  floatingActionButtonPosition: FabPosition = FabPosition.End,
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter(),
  bottom: @Composable () -> Unit = {},
  content: ScaffoldScope.() -> Content
) {
  UnscopedScaffold(
    modifier,
    topAppBar,
    floatingActionButton,
    floatingActionButtonPosition,
    snackbarPresenter,
    bottom
  ) {
    remember(::ScaffoldScope).content().value()
  }
}

/**
 * Orca-specific scaffold.
 *
 * @param modifier [Modifier] to be applied to the underlying [Scaffold].
 * @param topAppBar [TopAppBar] to be placed at the top.
 * @param floatingActionButton [FloatingActionButton] to be placed at the bottom, above the [bottom]
 *   and below the [SnackbarHost], horizontally centered.
 * @param floatingActionButtonPosition [FabPosition] that determines where the
 *   [floatingActionButton] will be placed.
 * @param snackbarPresenter [SnackbarPresenter] through which [Snackbar]s can be presented.
 * @param bottom [Composable] to be placed at the utmost bottom, such as a [ButtonBar].
 * @param content Main content of the current context.
 */
@Composable
fun UnscopedScaffold(
  modifier: Modifier = Modifier,
  topAppBar: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  floatingActionButtonPosition: FabPosition = FabPosition.End,
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter(),
  bottom: @Composable () -> Unit = {},
  content: @Composable () -> Unit
) {
  CompositionLocalProvider(
    LocalContainerColor provides
      LocalContainerColor.current.takeOrElse { AutosTheme.colors.background.container.asColor }
  ) {
    Scaffold(
      modifier,
      topAppBar,
      bottomBar = bottom,
      snackbarHost = {
        SnackbarHost(snackbarPresenter.hostState) {
          Snackbar(
            it,
            Modifier.testTag(SnackbarTag),
            shape = AutosTheme.forms.large.asShape,
            containerColor = it.orcaVisuals.containerColor
          )
        }
      },
      floatingActionButton,
      floatingActionButtonPosition,
      LocalContainerColor.current,
      contentWindowInsets = LocalWindowInsets.current.takeOrElse(WindowInsets::Zero)
    ) {
      Box(Modifier.background(LocalContainerColor.current).padding(it)) { content() }
    }
  }
}

/** Preview of an [UnscopedScaffold]. */
@Composable
@MultiThemePreview
private fun UnscopedScaffoldPreview() {
  val lazyListState = rememberLazyListState()
  val snackbarPresenter = rememberSnackbarPresenter()

  LaunchedEffect(snackbarPresenter) {
    snackbarPresenter.presentInfo("Info")
    snackbarPresenter.presentError("Error")
  }

  AutosTheme {
    UnscopedScaffold(
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
      LazyColumn(
        Modifier.fillMaxSize(),
        state = lazyListState,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = AutosTheme.overlays.fab.asPaddingValues
      ) {
        item { Text("Content", style = AutosTheme.typography.bodyMedium) }
      }
    }
  }
}
