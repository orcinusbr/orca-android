package com.jeanbarrossilva.orca.platform.autos.kit.scaffold

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.overlays.asPaddingValues
import com.jeanbarrossilva.orca.platform.autos.extensions.plus
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.Scaffold as _Scaffold
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.orcaVisuals
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.presenter.SnackbarPresenter
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.snack.presenter.rememberSnackbarPresenter
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/**
 * Orca-specific [Scaffold].
 *
 * @param modifier [Modifier] to be applied to the underlying [Scaffold].
 * @param topAppBar [TopAppBar] to be placed at the top.
 * @param floatingActionButton [FloatingActionButton] to be placed at the bottom, above the
 *   [buttonBar] and below the [SnackbarHost], horizontally centered.
 * @param floatingActionButtonPosition [FabPosition] that determines where the
 *   [floatingActionButton] will be placed.
 * @param snackbarPresenter [SnackbarPresenter] through which [Snackbar]s can be presented.
 * @param buttonBar [ButtonBar] to be placed at the utmost bottom.
 * @param content Main content of the current context.
 */
@Composable
fun Scaffold(
  modifier: Modifier = Modifier,
  topAppBar: @Composable () -> Unit = {},
  floatingActionButton: @Composable () -> Unit = {},
  floatingActionButtonPosition: FabPosition = FabPosition.End,
  snackbarPresenter: SnackbarPresenter = rememberSnackbarPresenter(),
  buttonBar: @Composable () -> Unit = {},
  content: @Composable (padding: PaddingValues) -> Unit
) {
  Scaffold(
    modifier,
    topAppBar,
    bottomBar = buttonBar,
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
    floatingActionButton = floatingActionButton,
    floatingActionButtonPosition = floatingActionButtonPosition,
    content = content
  )
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
      buttonBar = { ButtonBar(lazyListState) }
    ) {
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
