package com.jeanbarrossilva.orca.platform.theme

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
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.jeanbarrossilva.orca.platform.theme.configuration.Borders
import com.jeanbarrossilva.orca.platform.theme.configuration.LocalBorders
import com.jeanbarrossilva.orca.platform.theme.configuration.LocalOverlays
import com.jeanbarrossilva.orca.platform.theme.configuration.LocalShapes
import com.jeanbarrossilva.orca.platform.theme.configuration.LocalSpacings
import com.jeanbarrossilva.orca.platform.theme.configuration.Overlays
import com.jeanbarrossilva.orca.platform.theme.configuration.Shapes
import com.jeanbarrossilva.orca.platform.theme.configuration.Spacings
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.LocalColors
import com.jeanbarrossilva.orca.platform.theme.configuration.iconography.Iconography
import com.jeanbarrossilva.orca.platform.theme.configuration.iconography.LocalIconography
import com.jeanbarrossilva.orca.platform.theme.extensions.LocalTypography
import com.jeanbarrossilva.orca.platform.theme.extensions.Rubik
import com.jeanbarrossilva.orca.platform.theme.extensions.with

/** Provider of [OrcaTheme]'s configurations. */
object OrcaTheme {
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

  /** [Current][CompositionLocal.current] [Shapes] from [LocalShapes]. */
  val shapes
    @Composable get() = LocalShapes.current

  /** [Current][CompositionLocal.current] [Spacings] from [LocalSpacings]. */
  val spacings
    @Composable get() = LocalSpacings.current

  /** [Current][CompositionLocal.current] [Typography] from the underlying [MaterialTheme]. */
  val typography
    @Composable get() = MaterialTheme.typography
}

/**
 * [MaterialTheme] for Orca.
 *
 * @param content Content to be themed.
 */
@Composable
fun OrcaTheme(content: @Composable () -> Unit) {
  val view = LocalView.current
  val isPreviewing = remember(view) { view.isInEditMode }
  val themedContent =
    @Composable {
      Mdc3Theme(setTextColors = true, setDefaultFontFamily = true) {
        CompositionLocalProvider(
          LocalBorders provides Borders.default,
          LocalColors provides Colors.default,
          LocalIconography provides Iconography.default,
          LocalOverlays provides Overlays.Default,
          LocalShapes provides Shapes.default,
          LocalSpacings provides Spacings.default,
          LocalTextStyle provides OrcaTheme.typography.bodyMedium
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
   * current local one by a OrcaContextThemeWrapper that has the same configurations as
   * LocalContext.current but with Theme.Orca as its theme.
   */
  CompositionLocalProvider(
    LocalContext provides with(LocalContext.current) { remember { OrcaContextThemeWrapper(this) } },
    content = themedContent
  )
}
