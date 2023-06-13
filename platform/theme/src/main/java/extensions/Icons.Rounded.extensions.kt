package com.jeanbarrossilva.mastodonte.platform.theme.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/** Icon for backwards navigation that adapts to the direction of the layout. **/
val Icons.Rounded.backwardsNavigationArrow
    @Composable get() = when (LocalLayoutDirection.current) {
        LayoutDirection.Ltr -> KeyboardArrowLeft
        LayoutDirection.Rtl -> KeyboardArrowRight
    }

/** Icon for forwards navigation that adapts to the direction of the layout. **/
val Icons.Rounded.forwardsNavigationArrow
    @Composable get() = when (LocalLayoutDirection.current) {
        LayoutDirection.Ltr -> KeyboardArrowRight
        LayoutDirection.Rtl -> KeyboardArrowLeft
    }
