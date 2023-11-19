package com.jeanbarrossilva.orca.platform.autos.extensions

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize

/** Version of this [CornerBasedShape] with zeroed top [CornerSize]s. */
internal val CornerBasedShape.bottom
  get() = copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)

/**
 * Reversed version of this [CornerBasedShape], with its top and bottom [CornerSize]s switched. *
 */
internal val CornerBasedShape.reversed: CornerBasedShape
  get() =
    RoundedCornerShape(
      topStart = bottomStart,
      topEnd = bottomEnd,
      bottomEnd = topStart,
      bottomStart = topStart
    )

/** Version of this [CornerBasedShape] with zeroed bottom [CornerSize]s. */
internal val CornerBasedShape.top
  get() = copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize)
