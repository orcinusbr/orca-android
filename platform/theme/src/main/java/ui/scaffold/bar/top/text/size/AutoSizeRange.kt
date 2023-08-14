package com.jeanbarrossilva.orca.platform.theme.ui.scaffold.bar.top.text.size

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.jeanbarrossilva.orca.platform.theme.ui.scaffold.bar.top.text.AutoSizeText

/**
 * Range within which an [AutoSizeText] should be sized.
 *
 * @param density [Density] through which sizes will be converted from [SP][TextUnitType.Sp] to
 * [Float] and vice-versa.
 * @param min Minimum size.
 * @param max Maximum, default size.
 **/
@Immutable
data class AutoSizeRange internal constructor(
    private val density: Density,
    private val min: TextUnit,
    private val max: TextUnit
) : ClosedFloatingPointRange<Float> {
    override val start = with(density) { min.toPx() }
    override val endInclusive = with(density) { max.toPx() }

    init {
        require(min.type == max.type) {
            "Both minimum and maximum sizes should have the same TextUnitType."
        }
    }

    override fun lessThanOrEquals(a: Float, b: Float): Boolean {
        return with(density) {
            a.toSp() <= b.toSp()
        }
    }
}
