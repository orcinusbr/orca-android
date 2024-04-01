/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.autos.theme

import android.content.Context
import android.content.res.Configuration
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import br.com.orcinus.orca.autos.Spacings
import br.com.orcinus.orca.autos.borders.Borders
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.autos.forms.Forms
import br.com.orcinus.orca.autos.iconography.Iconography
import br.com.orcinus.orca.autos.overlays.Overlays
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.jeanbarrossilva.orca.platform.autos.borders.LocalBorders
import com.jeanbarrossilva.orca.platform.autos.colors.LocalColors
import com.jeanbarrossilva.orca.platform.autos.forms.LocalForms
import com.jeanbarrossilva.orca.platform.autos.iconography.LocalIconography
import com.jeanbarrossilva.orca.platform.autos.overlays.LocalOverlays
import com.jeanbarrossilva.orca.platform.autos.spacings.LocalSpacings

/** Provider of [AutosTheme]'s configurations. */
object AutosTheme {
  /** [Current][CompositionLocal.current] [Borders] from [LocalBorders]. */
  val borders
    @Composable get() = LocalBorders.current

  /** [Current][CompositionLocal.current] [Colors] from [LocalColors]. */
  val colors
    @Composable get() = LocalColors.current

  /** [Current][CompositionLocal.current] [Iconography] from [LocalIconography]. */
  val iconography
    @Composable get() = LocalIconography.current

  /** [Current][CompositionLocal.current] [Overlays] from [LocalOverlays]. */
  val overlays
    @Composable get() = LocalOverlays.current

  /** [Current][CompositionLocal.current] [Forms] from [LocalForms]. */
  val forms
    @Composable get() = LocalForms.current

  /** [Current][CompositionLocal.current] [Spacings] from [LocalSpacings]. */
  val spacings
    @Composable get() = LocalSpacings.current

  /** [Current][CompositionLocal.current] [Typography] from the underlying [MaterialTheme]. */
  val typography
    @Composable get() = MaterialTheme.typography

  /**
   * Gets the appropriate [Colors] based on the current system theme.
   *
   * @param context [Context] from which the theme will be checked.
   */
  fun getColors(context: Context): Colors {
    val isInDarkTheme =
      context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
        Configuration.UI_MODE_NIGHT_YES
    return if (isInDarkTheme) Colors.DARK else Colors.LIGHT
  }
}

/**
 * [MaterialTheme] for αὐτός.
 *
 * @param content Content to be themed.
 */
@Composable
fun AutosTheme(content: @Composable () -> Unit) {
  val context = LocalContext.current
  val view = LocalView.current
  val isPreviewing = remember(view) { view.isInEditMode }
  val themedContent =
    @Composable {
      val colors = remember(context) { AutosTheme.getColors(context) }
      val borders = remember(colors) { Borders.getDefault(colors) }

      Mdc3Theme(setTextColors = true, setDefaultFontFamily = true) {
        CompositionLocalProvider(
          LocalBorders provides borders,
          LocalColors provides colors,
          LocalIconography provides Iconography.default,
          LocalOverlays provides Overlays.default,
          LocalForms provides Forms.default,
          LocalSpacings provides Spacings.default,
          LocalTextStyle provides AutosTheme.typography.bodyMedium
        ) {
          /*
           * Mdc3Theme doesn't apply the font family specified in XML when previewing, so it
           * has to be set here. This isn't ideal because it requires us to do so twice, but
           * isn't a huge deal also, since it (hopefully) won't be changed that often.
           */
          if (isPreviewing) {
            CompositionLocalProvider(
              LocalTypography provides LocalTypography.current.with(FontFamily.Rubik),
              content = content
            )
          } else {
            content()
          }
        }
      }
    }

  /*
   * Since Mdc3Theme doesn't work with non-Material-3-themed Context instances, we replace the
   * current local one by an AutosContextThemeWrapper that has the same configurations as
   * LocalContext.current but with Theme.Autos as its theme.
   */
  CompositionLocalProvider(
    LocalContext provides
      with(LocalContext.current) { remember { AutosContextThemeWrapper(this) } },
    content = themedContent
  )
}
