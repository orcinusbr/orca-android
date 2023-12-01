/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
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
