package com.jeanbarrossilva.mastodon.feature.profiledetails.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.backwardsNavigationArrow

/**
 * Defines the availability of backwards navigation.
 *
 * @see Unavailable
 * @see Available
 **/
sealed class BackwardsNavigationState {
    /** Defines that backwards navigation is not available. **/
    object Unavailable : BackwardsNavigationState() {
        @Composable
        override fun Content(modifier: Modifier) {
        }
    }

    /**
     * Defines that backwards navigation is available, and thus can be performed through
     * [navigateBackwards].
     **/
    abstract class Available private constructor() : BackwardsNavigationState() {
        @Composable
        override fun Content(modifier: Modifier) {
            IconButton(onClick = ::navigateBackwards) {
                Icon(
                    MastodonteTheme.Icons.backwardsNavigationArrow,
                    contentDescription = "Back"
                )
            }
        }

        /** Navigates backwards, to the previous screen. **/
        internal abstract fun navigateBackwards()

        companion object {
            /**
             * Creates an [Available] instance.
             *
             * @param onBackwardsNavigation Action to be performed when [navigateBackwards] is
             * called.
             **/
            fun createInstance(onBackwardsNavigation: () -> Unit): Available {
                return object : Available() {
                    override fun navigateBackwards() {
                        onBackwardsNavigation()
                    }
                }
            }
        }
    }

    /**
     * [Composable] that represents the action that can be performed.
     *
     * @param modifier [Modifier] to be applied to the underlying [Composable].
     **/
    @Composable
    internal abstract fun Content(modifier: Modifier)
}

/** [Composable] that represents the action that can be performed. **/
@Composable
internal fun BackwardsNavigationState.Content() {
    Content(Modifier)
}
