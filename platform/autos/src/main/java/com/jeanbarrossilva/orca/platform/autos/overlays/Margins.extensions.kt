package com.jeanbarrossilva.orca.platform.autos.overlays

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.autos.overlays.Margins

/** [PaddingValues] version of these [Margins]. */
val Margins.asPaddingValues
  get() = PaddingValues(start.dp, top.dp, end.dp, bottom.dp)
