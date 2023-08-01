package com.jeanbarrossilva.orca.platform.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.jeanbarrossilva.orca.platform.theme.configuration.LocalOverlays
import com.jeanbarrossilva.orca.platform.theme.configuration.LocalSpacings
import com.jeanbarrossilva.orca.platform.theme.configuration.Overlays
import com.jeanbarrossilva.orca.platform.theme.configuration.Spacings
import com.jeanbarrossilva.orca.platform.theme.extensions.LocalTypography
import com.jeanbarrossilva.orca.platform.theme.extensions.Rubik
import com.jeanbarrossilva.orca.platform.theme.extensions.bottom
import com.jeanbarrossilva.orca.platform.theme.extensions.end
import com.jeanbarrossilva.orca.platform.theme.extensions.start
import com.jeanbarrossilva.orca.platform.theme.extensions.top
import com.jeanbarrossilva.orca.platform.theme.extensions.with

/** Height of [ColorSchemePreview]. **/
private const val COLOR_SCHEME_PREVIEW_HEIGHT = 1_868

/** Height of [ShapesPreview]. **/
private const val SHAPES_PREVIEW_HEIGHT = 909

/** Height of [TypographyPreview]. **/
private const val TYPOGRAPHY_PREVIEW_HEIGHT = 1_135

/** Provider of [OrcaTheme]'s configurations. **/
object OrcaTheme {
    /**
     * [Icons][androidx.compose.material.icons.Icons] in the chosen style. Alias for
     * [Icons.Rounded].
     **/
    val Icons = androidx.compose.material.icons.Icons.Rounded

    /** [Current][CompositionLocal.current] [ColorScheme] from the underlying [MaterialTheme]. **/
    val colorScheme
        @Composable get() = MaterialTheme.colorScheme

    /** [Current][CompositionLocal.current] [Overlays] from [LocalOverlays]. **/
    val overlays
        @Composable get() = LocalOverlays.current

    /** [Current][CompositionLocal.current] [Shapes] from the underlying [MaterialTheme]. **/
    val shapes
        @Composable get() = MaterialTheme.shapes

    /** [Current][CompositionLocal.current] [Spacings] from [LocalSpacings]. **/
    val spacings
        @Composable get() = LocalSpacings.current

    /** [Current][CompositionLocal.current] [Typography] from the underlying [MaterialTheme]. **/
    val typography
        @Composable get() = MaterialTheme.typography
}

/**
 * [MaterialTheme] for Orca.
 *
 * @param content Content to be themed.
 **/
@Composable
fun OrcaTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    val isPreviewing = remember(view) { view.isInEditMode }
    val themedContent = @Composable {
        Mdc3Theme(setTextColors = true, setDefaultFontFamily = true) {
            CompositionLocalProvider(
                LocalOverlays provides Overlays.Default,
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
        LocalContext provides with(LocalContext.current) {
            remember {
                OrcaContextThemeWrapper(this)
            }
        },
        content = themedContent
    )
}

/** Preview of [OrcaTheme]'s [ColorScheme]. **/
@Composable
@Preview(heightDp = COLOR_SCHEME_PREVIEW_HEIGHT)
private fun ColorSchemePreview() {
    OrcaTheme {
        Column(Modifier.fillMaxWidth()) {
            ColorSchemeSection("Primary") {
                Color(OrcaTheme.colorScheme.primary)
                Color(OrcaTheme.colorScheme.inversePrimary)
                Color(OrcaTheme.colorScheme.onPrimary)
            }

            ColorSchemeSection("Primary container") {
                Color(OrcaTheme.colorScheme.primaryContainer)
                Color(OrcaTheme.colorScheme.onPrimaryContainer)
            }

            ColorSchemeSection("Secondary") {
                Color(OrcaTheme.colorScheme.secondary)
                Color(OrcaTheme.colorScheme.onSecondary)
            }

            ColorSchemeSection("Secondary container") {
                Color(OrcaTheme.colorScheme.secondaryContainer)
                Color(OrcaTheme.colorScheme.onSecondaryContainer)
            }

            ColorSchemeSection("Tertiary") {
                Color(OrcaTheme.colorScheme.tertiary)
                Color(OrcaTheme.colorScheme.onTertiary)
            }

            ColorSchemeSection("Tertiary container") {
                Color(OrcaTheme.colorScheme.tertiaryContainer)
                Color(OrcaTheme.colorScheme.onTertiaryContainer)
            }

            ColorSchemeSection("Background") {
                Color(OrcaTheme.colorScheme.background)
                Color(OrcaTheme.colorScheme.onBackground)
            }

            ColorSchemeSection("Surface") {
                Color(OrcaTheme.colorScheme.surface)
                Color(OrcaTheme.colorScheme.inverseSurface)
                Color(OrcaTheme.colorScheme.onSurface)
                Color(OrcaTheme.colorScheme.inverseOnSurface)
                Color(OrcaTheme.colorScheme.surfaceTint)
            }

            ColorSchemeSection("Surface variant") {
                Color(OrcaTheme.colorScheme.surfaceVariant)
                Color(OrcaTheme.colorScheme.onSurfaceVariant)
            }

            ColorSchemeSection("Error") {
                Color(OrcaTheme.colorScheme.error)
                Color(OrcaTheme.colorScheme.onError)
            }

            ColorSchemeSection("Error container") {
                Color(OrcaTheme.colorScheme.errorContainer)
                Color(OrcaTheme.colorScheme.onErrorContainer)
            }

            ColorSchemeSection("Outline") {
                Color(OrcaTheme.colorScheme.outline)
            }

            ColorSchemeSection("Outline variant") {
                Color(OrcaTheme.colorScheme.outlineVariant)
            }

            ColorSchemeSection("Scrim") {
                Color(OrcaTheme.colorScheme.scrim)
            }
        }
    }
}

/** Preview of [OrcaTheme]'s [Overlays]. **/
@Composable
@Preview
private fun OverlaysPreview() {
    OrcaTheme {
        Surface(color = OrcaTheme.colorScheme.background) {
            OverlaySection("FAB", OrcaTheme.overlays.fab)
        }
    }
}

/** Preview of [OrcaTheme]'s [Shapes]. **/
@Composable
@Preview(heightDp = SHAPES_PREVIEW_HEIGHT)
private fun ShapesPreview() {
    OrcaTheme {
        Surface(Modifier.fillMaxWidth(), color = OrcaTheme.colorScheme.background) {
            Column {
                ShapeSection("Extra large", OrcaTheme.shapes.extraLarge)
                ShapeSection("Large", OrcaTheme.shapes.large)
                ShapeSection("Medium", OrcaTheme.shapes.medium)
                ShapeSection("Small", OrcaTheme.shapes.small)
                ShapeSection("Extra small", OrcaTheme.shapes.extraSmall)
            }
        }
    }
}

/** Preview of [OrcaTheme]'s [Spacings]. **/
@Composable
@Preview
private fun SpacingsPreview() {
    OrcaTheme {
        Surface(Modifier.fillMaxWidth()) {
            Column {
                SpacingSection("Extra large", OrcaTheme.spacings.extraLarge)
                SpacingSection("Large", OrcaTheme.spacings.large)
                SpacingSection("Medium", OrcaTheme.spacings.medium)
                SpacingSection("Small", OrcaTheme.spacings.small)
                SpacingSection("Extra small", OrcaTheme.spacings.extraSmall)
            }
        }
    }
}

/** Preview of [OrcaTheme]'s [Typography]. **/
@Composable
@Preview(heightDp = TYPOGRAPHY_PREVIEW_HEIGHT)
private fun TypographyPreview() {
    OrcaTheme {
        Surface(Modifier.fillMaxWidth(), color = OrcaTheme.colorScheme.background) {
            Column {
                TypographySection("Display") {
                    Text("D1", style = OrcaTheme.typography.displayLarge)
                    Text("D2", style = OrcaTheme.typography.displayMedium)
                    Text("D3", style = OrcaTheme.typography.displaySmall)
                }

                TypographySection("Headline") {
                    Text("H1", style = OrcaTheme.typography.headlineLarge)
                    Text("H2", style = OrcaTheme.typography.headlineMedium)
                    Text("H3", style = OrcaTheme.typography.headlineSmall)
                }

                TypographySection("Title") {
                    Text("T1", style = OrcaTheme.typography.titleLarge)
                    Text("T2", style = OrcaTheme.typography.titleMedium)
                    Text("T3", style = OrcaTheme.typography.titleSmall)
                }

                TypographySection("Body") {
                    Text("B1", style = OrcaTheme.typography.bodyLarge)
                    Text("B2", style = OrcaTheme.typography.bodyMedium)
                    Text("B3", style = OrcaTheme.typography.bodySmall)
                }

                TypographySection("Label") {
                    Text("L1", style = OrcaTheme.typography.labelLarge)
                    Text("L2", style = OrcaTheme.typography.labelMedium)
                    Text("L3", style = OrcaTheme.typography.labelSmall)
                }
            }
        }
    }
}

/**
 * [Section] that displays the [Color][com.jeanbarrossilva.orca.platform.theme.Color]s provided in
 * the [content].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param modifier [Modifier] to be applied to the underlying [Section].
 * @param content [Color][com.jeanbarrossilva.orca.platform.theme.Color]s to be shown.
 **/
@Composable
private fun ColorSchemeSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Section(title, modifier) {
        Row(content = content)
    }
}

/**
 * [Box] that displays the given [color].
 *
 * @param color [Color] to be displayed.
 **/
@Composable
private fun Color(color: Color) {
    Box(
        Modifier
            .background(color)
            .size(64.dp)
    )
}

/**
 * [Section] that displays the given [overlay].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param overlay Overlay to be displayed.
 * @param modifier [Modifier] to be applied to the underlying [Section].
 **/
@Composable
private fun OverlaySection(title: String, overlay: PaddingValues, modifier: Modifier = Modifier) {
    Section(title, modifier) {
        Text(
            "[${overlay.start}, ${overlay.top}, ${overlay.end}, ${overlay.bottom}]",
            Modifier.padding(OrcaTheme.spacings.large),
            style = OrcaTheme.typography.titleMedium
        )
    }
}

/**
 * [Section] that displays the given [shape].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param shape [Shape] to be displayed.
 * @param modifier [Modifier] to be applied to the underlying [Section].
 **/
@Composable
private fun ShapeSection(title: String, shape: Shape, modifier: Modifier = Modifier) {
    Section(title, modifier) {
        Box(
            Modifier
                .padding(it)
                .clip(shape)
                .background(OrcaTheme.colorScheme.primaryContainer)
                .height(64.dp)
                .fillMaxWidth()
        )
    }
}

/**
 * [Section] that displays the given [spacing].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param spacing Spacing to be displayed.
 * @param modifier [Modifier] to be applied to the underlying [Section].
 **/
@Composable
private fun SpacingSection(title: String, spacing: Dp, modifier: Modifier = Modifier) {
    Section(title, modifier) { padding ->
        Text(
            "$spacing",
            Modifier
                .padding(start = padding.start, top = spacing, end = padding.end, bottom = spacing),
            style = OrcaTheme.typography.titleMedium
        )
    }
}

/**
 * [Section] that displays the [Text]s in the given [content].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param modifier [Modifier] to be applied to the underlying [Section].
 * @param content [Text]s to be shown.
 **/
@Composable
private fun TypographySection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Section(title, modifier) {
        Column(
            modifier.padding(it),
            Arrangement.spacedBy(OrcaTheme.spacings.small),
            content = content
        )
    }
}

/**
 * Displays a header with the given [title] followed by the [content].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param modifier [Modifier] to be applied to the underlying [Column].
 * @param content Content to be displayed. Receives the same padding that's applied to the header.
 **/
@Composable
private fun Section(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(padding: PaddingValues) -> Unit
) {
    val padding = PaddingValues(OrcaTheme.spacings.large)

    Column(modifier) {
        Text(
            title,
            Modifier
                .background(OrcaTheme.colorScheme.surfaceVariant)
                .padding(padding)
                .fillMaxWidth(),
            OrcaTheme.colorScheme.onSurfaceVariant,
            style = OrcaTheme.typography.titleMedium
        )

        content(padding)
    }
}
