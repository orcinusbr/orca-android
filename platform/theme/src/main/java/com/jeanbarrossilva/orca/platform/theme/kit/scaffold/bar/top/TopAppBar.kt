package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top

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
 * @param title Short explanation of what's being presented by the overall content.
 * @param modifier [Modifier] to be applied to the underlying [TopAppBar].
 * @param navigationIcon [HoverableIconButton] through which navigation can be performed, usually
 * for popping the back stack.
 * @param subtitle Contextualizes the [title].
 * @param actions [IconButton]s with actions to be performed in this context.
 * @param scrollBehavior Defines how this [TopAppBar][_TopAppBar] behaves on scroll.
 **/
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = { },
    subtitle: @Composable () -> Unit = { },
    actions: @Composable RowScope.() -> Unit = { },
    scrollBehavior: TopAppBarScrollBehavior = _TopAppBarDefaults.scrollBehavior
) {
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
    val idleContainerColor = OrcaTheme.colors.background.container
    val scrolledContainerColor = OrcaTheme.colors.surface.container
    val containerColorTransitionFraction = remember(overlap) { if (overlap > 0.01f) 1f else 0f }
    val containerColor by animateColorAsState(
        lerp(
            idleContainerColor,
            scrolledContainerColor,
            FastOutLinearInEasing.transform(containerColorTransitionFraction)
        ),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "ContainerColor"
    )
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

                Column {
                    ProvideTextStyle(OrcaTheme.typography.titleSmall, subtitle)
                    ProvideTextStyle(OrcaTheme.typography.headlineLarge, title)
                }
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
            containerColor = idleContainerColor,
            scrolledContainerColor,
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
            title = { Text("Title") },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(OrcaTheme.iconography.back, contentDescription = "Back")
                }
            },
            subtitle = { Text("Subtitle") },
            actions = {
                HoverableIconButton(onClick = { }) {
                    Icon(OrcaTheme.iconography.search, contentDescription = "Search")
                }
            }
        )
    }
}
