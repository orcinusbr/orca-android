package com.jeanbarrossilva.orca.platform.theme.configuration

import android.content.Context
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.jeanbarrossilva.orca.platform.theme.R
import com.jeanbarrossilva.orca.platform.theme.extensions.getFraction

/** [CompositionLocal] that provides [Shapes]. */
internal val LocalShapes = compositionLocalOf { Shapes.Unspecified }

/**
 * [CornerBasedShape]s by which UI components throughout the application are clipped.
 *
 * @param large [CornerBasedShape] for large components.
 * @param medium [CornerBasedShape] for medium-sized components
 * @param small [CornerBasedShape] for small components..
 */
data class Shapes
internal constructor(
  val large: CornerBasedShape,
  val medium: CornerBasedShape,
  val small: CornerBasedShape
) {
  companion object {
    /** [Shapes] with unspecified [CornerBasedShape]s. */
    internal val Unspecified =
      Shapes(
        large = RoundedCornerShape(Dp.Unspecified),
        medium = RoundedCornerShape(Dp.Unspecified),
        small = RoundedCornerShape(Dp.Unspecified)
      )

    /** [Shapes] that are provided by default. */
    internal val default
      @Composable get() = getDefault(LocalContext.current)

    /**
     * Gets the [Shapes] that are provided by default.
     *
     * @param context [Context] from which the [Shapes] will be obtained.
     */
    internal fun getDefault(context: Context): Shapes {
      return Shapes(
        RoundedCornerShape(context.resources.getDimension(R.dimen.largeShapeCornerSize)),
        RoundedCornerShape(context.resources.getDimension(R.dimen.mediumShapeCornerSize)),
        RoundedCornerShape(
          (context.resources.getFraction(R.fraction.smallShapeCornerSize) ?: 0f) * 100f
        )
      )
    }
  }
}
