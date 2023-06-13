package com.jeanbarrossilva.mastodonte.platform.theme.extensions

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape

/** Reversed version of this [CornerBasedShape], with its top and bottom [CornerSize]s switched. **/
internal val CornerBasedShape.reversed: CornerBasedShape
    get() = RoundedCornerShape(
        topStart = bottomStart,
        topEnd = bottomEnd,
        bottomEnd = topStart,
        bottomStart = topStart
    )
