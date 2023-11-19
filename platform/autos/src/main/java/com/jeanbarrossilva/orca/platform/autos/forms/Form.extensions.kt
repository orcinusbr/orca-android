package com.jeanbarrossilva.orca.platform.autos.forms

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.forms.Form

/** [Shape] version of this [Form]. */
val Form.asShape: CornerBasedShape
  get() =
    when (this) {
      is Form.PerCorner -> RoundedCornerShape(topStart.dp, topEnd.dp, bottomEnd.dp, bottomStart.dp)
      is Form.Percent -> RoundedCornerShape(percentage * 100)
    }
