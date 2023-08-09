package com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar

import android.content.res.Configuration
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.backwardsNavigationArrow
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.TopAppBar as _TopAppBar
import com.jeanbarrossilva.orca.platform.ui.component.scaffold.bar.TopAppBarDefaults as _TopAppBarDefaults

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
 * @param navigationIcon [IconButton] through which navigation can be performed, usually for popping
 * the back stack.
 * @param actions [IconButton]s with actions to be performed in this context.
 * @param scrollBehavior Defines how this [TopAppBar][_TopAppBar] behaves on scroll.
 * @param title Short explanation of what's being presented by the overall content.
 * @see Icons.Rounded.backwardsNavigationArrow
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
    val overlap = scrollBehavior.state.overlappedFraction
    val isOverlapping = remember(overlap) { overlap > 0f }
    val spacing = OrcaTheme.spacings.medium - OrcaTheme.spacings.small
    val verticalSpacing by animateDpAsState(
        if (isOverlapping) 0.dp else spacing,
        label = "VerticalSpacing"
    )
    val elevation by animateDpAsState(if (isOverlapping) 4.dp else 0.dp, label = "Elevation")

    TopAppBar(
        title = {
            Row {
                Spacer(Modifier.width(spacing))
                ProvideTextStyle(OrcaTheme.typography.displayLarge, title)
            }
        },
        modifier
            .shadow(elevation)
            .background(containerColor)
            .padding(vertical = verticalSpacing),
        navigationIcon = {
            Row {
                Spacer(Modifier.width(spacing))
                navigationIcon()
            }
        },
        actions,
        colors = TopAppBarDefaults.topAppBarColors(scrolledContainerColor = containerColor),
        scrollBehavior = scrollBehavior
    )
}

/** Preview of a [TopAppBar][_TopAppBar]. **/
@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun TopAppBarPreview() {
    OrcaTheme {
        @OptIn(ExperimentalMaterial3Api::class)
        _TopAppBar(
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(OrcaTheme.Icons.backwardsNavigationArrow, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Icon(OrcaTheme.Icons.Search, contentDescription = "Search")
                }
            }
        ) {
            Text("Title")
        }
    }
}
