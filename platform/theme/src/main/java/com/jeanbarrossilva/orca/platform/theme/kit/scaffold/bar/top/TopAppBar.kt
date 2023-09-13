package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.Borders
import com.jeanbarrossilva.orca.platform.theme.extensions.`if`
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.HoverableIconButton
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBar as _TopAppBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBarDefaults as _TopAppBarDefaults

/** Default values of a [TopAppBar][_TopAppBar]. **/
object TopAppBarDefaults {
    /** [TopAppBarScrollBehavior] adopted by default by a [TopAppBar][_TopAppBar]. **/
    val scrollBehavior
        @Composable
        @OptIn(ExperimentalMaterial3Api::class)
        get() = TopAppBarDefaults.enterAlwaysScrollBehavior()
}

/**
 * App bar for the top portion of the screen, with a [title] and [actions] that are specifically
 * related to the current context.
 *
 * @param modifier [Modifier] to be applied to the underlying [TopAppBar].
 * @param navigationIcon [HoverableIconButton] through which navigation can be performed, usually
 * for popping the back stack.
 * @param actions [IconButton]s with actions to be performed in this context.
 * @param scrollBehavior Defines how this [TopAppBar][_TopAppBar] behaves on scroll.
 * @param title Short explanation of what's being presented by the overall content.
 **/
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = { },
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = _TopAppBarDefaults.scrollBehavior,
    title: @Composable () -> Unit
) {
    val containerColor = OrcaTheme.colors.surface.container
    val overlap by remember(scrollBehavior) {
        derivedStateOf {
            scrollBehavior.state.overlappedFraction
        }
    }
    val heightOffset by remember(scrollBehavior) {
        derivedStateOf {
            scrollBehavior.state.heightOffset
        }
    }
    val isOverlapping = remember(overlap) { overlap > 0f }
    val spacing = OrcaTheme.spacings.medium
    val verticalSpacing by animateDpAsState(
        with(LocalDensity.current) { maxOf(0.dp, spacing + heightOffset.toDp()) },
        label = "VerticalSpacing"
    )
    val borderStrokeWidth by animateDpAsState(
        if (isOverlapping) OrcaTheme.borders.default.width else 0.dp,
        label = "BorderStrokeWidth"
    )

    TopAppBar(
        title = {
            Row {
                Spacer(Modifier.width(spacing))
                ProvideTextStyle(OrcaTheme.typography.displayLarge, title)
            }
        },
        modifier
            .`if`(Borders.areApplicable) {
                border(OrcaTheme.borders.default.copy(width = borderStrokeWidth))
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
        colors = TopAppBarDefaults.topAppBarColors(
            scrolledContainerColor = containerColor,
            actionIconContentColor = OrcaTheme.colors.secondary
        ),
        scrollBehavior = scrollBehavior
    )
}

/** Preview of a [TopAppBar][_TopAppBar]. **/
@Composable
@MultiThemePreview
private fun TopAppBarPreview() {
    OrcaTheme {
        @OptIn(ExperimentalMaterial3Api::class)
        _TopAppBar(
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(OrcaTheme.iconography.back, contentDescription = "Back")
                }
            },
            actions = {
                HoverableIconButton(onClick = { }) {
                    Icon(OrcaTheme.iconography.search, contentDescription = "Search")
                }
            }
        ) {
            Text("Title")
        }
    }
}
