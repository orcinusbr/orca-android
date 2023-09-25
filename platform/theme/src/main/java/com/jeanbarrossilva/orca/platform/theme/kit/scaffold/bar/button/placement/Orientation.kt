package com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.placement

import androidx.compose.material3.Button
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.IntOffset
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.ButtonBar

/** Indicates how [Button]s in a [ButtonBar] are placed in relation to each other. **/
internal enum class Orientation {
    /** [Button]s are placed below each other. **/
    VERTICAL {
        override fun getOffset(placement: Placement): IntOffset {
            return IntOffset(x = 0, y = placement.axisOffset)
        }

        override fun getDimension(placeable: Placeable): Int {
            return placeable.height
        }
    };

    /**
     * Gets the [IntOffset] by which the [placement] will be shifted in the axis that's appropriate
     * for this specific [Orientation].
     **/
    abstract fun getOffset(placement: Placement): IntOffset

    /**
     * Gets the dimension (width or height) to be considered when placing the [placeable].
     *
     * @param placeable [Placeable] whose appropriate dimension will be obtained.
     **/
    abstract fun getDimension(placeable: Placeable): Int
}
