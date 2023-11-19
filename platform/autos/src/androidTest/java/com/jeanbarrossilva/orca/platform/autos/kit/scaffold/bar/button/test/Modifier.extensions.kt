package com.jeanbarrossilva.orca.platform.autos.kit.scaffold.bar.button.test

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

/** Makes the content fill the screen size. */
internal fun Modifier.fillScreenSize(): Modifier {
  return composed {
    with(LocalContext.current.resources.configuration) {
      height(screenHeightDp.dp).width(screenWidthDp.dp)
    }
  }
}
