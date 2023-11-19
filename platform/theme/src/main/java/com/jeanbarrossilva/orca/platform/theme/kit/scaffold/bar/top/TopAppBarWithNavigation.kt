package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.R
import com.jeanbarrossilva.orca.platform.theme.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.HoverableIconButton

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
      OrcaTheme.iconography.back.asImageVector,
      contentDescription =
        stringResource(R.string.platform_ui_top_app_bar_with_back_navigation_action)
    )
  }
}
