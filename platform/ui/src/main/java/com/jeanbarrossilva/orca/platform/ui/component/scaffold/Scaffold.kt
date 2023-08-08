package com.jeanbarrossilva.orca.platform.ui.component.scaffold

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.TopAppBar
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.text.AutoSizeText
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.orcaVisuals
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.showErrorSnackbar
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.snackbar.showInfoSnackbar

/**
 * Orca-specific [Scaffold].
 *
 * @param modifier [Modifier] to be applied to the underlying [Scaffold].
 * @param floatingActionButton [FloatingActionButton] to be placed at the bottom, horizontally
 * centered.
 * @param topAppBar [TopAppBar] to be placed at the top.
 * @param snackbarHostState [SnackbarHostState] through which a [Snackbar] can be shown. Note that
 * simply utilizing [SnackbarHostState.showSnackbar] will not work, so it should be requested to be
 * made visible via extensions provided by this module (such as [SnackbarHostState.showInfoSnackbar]
 * and [SnackbarHostState.showErrorSnackbar]).
 * @param content Main content of the current context.
 **/
@Composable
fun Scaffold(
    modifier: Modifier = Modifier,
    floatingActionButton: @Composable () -> Unit,
    topAppBar: @Composable () -> Unit = { },
    snackbarHostState: SnackbarHostState = remember(::SnackbarHostState),
    content: @Composable (padding: PaddingValues) -> Unit
) {
    Scaffold(
        modifier,
        topAppBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    it,
                    shape = OrcaTheme.shapes.medium,
                    containerColor = it.orcaVisuals.containerColor,
                    dismissActionContentColor = it.orcaVisuals.contentColor
                )
            }
        },
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        content = content
    )
}

/** Preview of a [Scaffold][com.jeanbarrossilva.orca.platform.ui.component.scaffold.Scaffold]. **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ScaffoldPreview() {
    val snackbarHostState = remember(::SnackbarHostState)

    LaunchedEffect(snackbarHostState) {
        snackbarHostState.showInfoSnackbar("Info")
        snackbarHostState.showErrorSnackbar("Error")
    }

    OrcaTheme {
        @Suppress("UnusedMaterial3ScaffoldPaddingParameter")
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { }) {
                    Icon(OrcaTheme.Icons.Add, contentDescription = "Add")
                }
            },
            topAppBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar {
                    AutoSizeText("Scaffold")
                }
            },
            snackbarHostState = snackbarHostState
        ) {
        }
    }
}
