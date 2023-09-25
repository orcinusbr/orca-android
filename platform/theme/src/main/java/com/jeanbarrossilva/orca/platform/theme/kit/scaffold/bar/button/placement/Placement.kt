package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement

import androidx.compose.ui.layout.Placeable

/**
 * Indicates where the [placeable] will be placed in relation to previously placed ones.
 *
 * @param placeable [Placeable] yet to be placed.
 * @param axisOffset Additional horizontal or vertical shift in pixels.
 **/
internal data class Placement(val placeable: Placeable, val axisOffset: Int)
