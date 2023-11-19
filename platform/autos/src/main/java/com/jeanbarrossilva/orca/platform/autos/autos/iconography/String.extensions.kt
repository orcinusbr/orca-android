package com.jeanbarrossilva.orca.platform.autos.autos.iconography

import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource

/** [ImageVector] version of this icon ID. */
val String.asImageVector
  @Composable
  @Suppress("DiscouragedApi")
  get() =
    with("icon_${replace('-', '_')}") id@{
      with(LocalContext.current) {
        try {
          ImageVector.vectorResource(
            theme,
            resources,
            resources.getIdentifier(this@id, "drawable", packageName)
          )
        } catch (_: Resources.NotFoundException) {
          throw Resources.NotFoundException(this@id)
        }
      }
    }
