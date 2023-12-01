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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.borders.Borders
import com.jeanbarrossilva.orca.platform.autos.borders.areApplicable
import com.jeanbarrossilva.orca.platform.autos.borders.asBorderStroke
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.iconography.asImageVector
import com.jeanbarrossilva.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBar as _TopAppBar
import com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.top.TopAppBarDefaults as _TopAppBarDefaults
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview

/** Default values of a [TopAppBar][_TopAppBar]. */
object TopAppBarDefaults {
  /** [TopAppBarScrollBehavior] adopted by default by a [TopAppBar][_TopAppBar]. */
  val scrollBehavior
    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    get() = TopAppBarDefaults.enterAlwaysScrollBehavior()
}

/**
 * App bar for the top portion of the screen, with a [title] and [actions] that are specifically
 * related to the current context.
 *
 * @param title Short explanation of what's being presented by the overall content.
 * @param modifier [Modifier] to be applied to the underlying [TopAppBar].
 * @param navigationIcon [HoverableIconButton] through which navigation can be performed, usually
 *   for popping the back stack.
 * @param subtitle Contextualizes the [title].
 * @param actions [IconButton]s with actions to be performed in this context.
 * @param scrollBehavior Defines how this [TopAppBar][_TopAppBar] behaves on scroll.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBar(
  title: @Composable () -> Unit,
  modifier: Modifier = Modifier,
  navigationIcon: @Composable () -> Unit = {},
  subtitle: @Composable () -> Unit = {},
  actions: @Composable RowScope.() -> Unit = {},
  scrollBehavior: TopAppBarScrollBehavior = _TopAppBarDefaults.scrollBehavior
) {
  val overlap by
    remember(scrollBehavior) { derivedStateOf { scrollBehavior.state.overlappedFraction } }
  val heightOffset by
    remember(scrollBehavior) { derivedStateOf { scrollBehavior.state.heightOffset } }
  val isOverlapping = remember(overlap) { overlap > 0f }
  val idleContainerColor = AutosTheme.colors.background.container.asColor
  val scrolledContainerColor = AutosTheme.colors.surface.container.asColor
  val containerColorTransitionFraction = remember(overlap) { if (overlap > 0.01f) 1f else 0f }
  val containerColor by
    animateColorAsState(
      lerp(
        idleContainerColor,
        scrolledContainerColor,
        FastOutLinearInEasing.transform(containerColorTransitionFraction)
      ),
      animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
      label = "ContainerColor"
    )
  val spacing = AutosTheme.spacings.medium.dp
  val verticalSpacing by
    animateDpAsState(
      with(LocalDensity.current) { maxOf(0.dp, spacing + heightOffset.toDp()) },
      label = "VerticalSpacing"
    )
  val borderStrokeWidth by
    animateDpAsState(
      if (isOverlapping) AutosTheme.borders.default.asBorderStroke.width else 0.dp,
      label = "BorderStrokeWidth"
    )

  TopAppBar(
    title = {
      Row {
        Spacer(Modifier.width(spacing))

        Column {
          ProvideTextStyle(AutosTheme.typography.titleSmall, subtitle)
          ProvideTextStyle(AutosTheme.typography.headlineLarge, title)
        }
      }
    },
    modifier
      .`if`(Borders.areApplicable) {
        border(AutosTheme.borders.default.asBorderStroke.copy(width = borderStrokeWidth))
      }
      .background(containerColor)
      .padding(vertical = verticalSpacing),
    navigationIcon = {
      Row {
        Spacer(Modifier.width(spacing))
        navigationIcon()
      }
    },
    actions = {
      Row {
        actions()
        Spacer(Modifier.width(spacing))
      }
    },
    colors =
      TopAppBarDefaults.topAppBarColors(
        containerColor = idleContainerColor,
        scrolledContainerColor,
        actionIconContentColor = AutosTheme.colors.secondary.asColor
      ),
    scrollBehavior = scrollBehavior
  )
}

/** Preview of a [TopAppBar][_TopAppBar]. */
@Composable
@MultiThemePreview
private fun TopAppBarPreview() {
  AutosTheme {
    @OptIn(ExperimentalMaterial3Api::class)
    _TopAppBar(
      title = { Text("Title") },
      navigationIcon = {
        IconButton(onClick = {}) {
          Icon(AutosTheme.iconography.back.asImageVector, contentDescription = "Back")
        }
      },
      subtitle = { Text("Subtitle") },
      actions = {
        HoverableIconButton(onClick = {}) {
          Icon(AutosTheme.iconography.search.asImageVector, contentDescription = "Search")
        }
      }
    )
  }
}
