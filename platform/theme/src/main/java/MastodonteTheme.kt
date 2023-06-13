package com.jeanbarrossilva.mastodonte.platform.theme

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
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.LocalOverlays
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.LocalSpacings
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.Overlays
import com.jeanbarrossilva.mastodonte.platform.theme.configuration.Spacings
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Rubik
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.bottom
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.colorAttribute
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.end
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.start
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.top
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.with

/** Height of [ColorSchemePreview]. **/
private const val COLOR_SCHEME_PREVIEW_HEIGHT = 1_884

/** Height of [ShapesPreview]. **/
private const val SHAPES_PREVIEW_HEIGHT = 898

/** Height of [TypographyPreview]. **/
private const val TYPOGRAPHY_PREVIEW_HEIGHT = 1_130

/** [android.R.attr.colorControlNormal] with medium visibility. **/
private val fadedContentColor
    @Composable get() = colorAttribute(android.R.attr.colorControlNormal).copy(alpha = .5f)

/** Provider of [MastodonteTheme]'s configurations. **/
object MastodonteTheme {
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
 * [MaterialTheme] for Tick.
 *
 * @param content Content to be themed.
 **/
@Composable
fun MastodonteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primaryContainer = Color(0xFF1A1A1A),
            onPrimaryContainer = Color(0xFFB388FF),
            background = Color.Black,
            surface = Color(0xFF3D3D3D),
            surfaceVariant = Color(0xFF202020)
        ),
        typography = with(Typography() with FontFamily.Rubik) {
            copy(
                displayLarge = displayLarge.copy(
                    color = fadedContentColor,
                    fontWeight = FontWeight.Black
                ),
                titleLarge = titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                titleMedium = titleSmall.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                titleSmall = titleSmall.copy(color = fadedContentColor, fontSize = 18.sp),
                bodyLarge = bodyLarge.copy(fontWeight = FontWeight.Bold),
                labelLarge = labelMedium.copy(
                    color = fadedContentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    ) {
        CompositionLocalProvider(
            LocalOverlays provides Overlays.Default,
            LocalSpacings provides Spacings.default,
            LocalTextStyle provides MastodonteTheme.typography.bodyMedium,
            content = content
        )
    }
}

/** Preview of [MastodonteTheme]'s [ColorScheme]. **/
@Composable
@Preview(heightDp = COLOR_SCHEME_PREVIEW_HEIGHT)
private fun ColorSchemePreview() {
    MastodonteTheme {
        Column(Modifier.fillMaxWidth()) {
            ColorSchemeSection("Primary") {
                Color(MastodonteTheme.colorScheme.primary)
                Color(MastodonteTheme.colorScheme.inversePrimary)
                Color(MastodonteTheme.colorScheme.onPrimary)
            }

            ColorSchemeSection("Primary container") {
                Color(MastodonteTheme.colorScheme.primaryContainer)
                Color(MastodonteTheme.colorScheme.onPrimaryContainer)
            }

            ColorSchemeSection("Secondary") {
                Color(MastodonteTheme.colorScheme.secondary)
                Color(MastodonteTheme.colorScheme.onSecondary)
            }

            ColorSchemeSection("Secondary container") {
                Color(MastodonteTheme.colorScheme.secondaryContainer)
                Color(MastodonteTheme.colorScheme.onSecondaryContainer)
            }

            ColorSchemeSection("Tertiary") {
                Color(MastodonteTheme.colorScheme.tertiary)
                Color(MastodonteTheme.colorScheme.onTertiary)
            }

            ColorSchemeSection("Tertiary container") {
                Color(MastodonteTheme.colorScheme.tertiaryContainer)
                Color(MastodonteTheme.colorScheme.onTertiaryContainer)
            }

            ColorSchemeSection("Background") {
                Color(MastodonteTheme.colorScheme.background)
                Color(MastodonteTheme.colorScheme.onBackground)
            }

            ColorSchemeSection("Surface") {
                Color(MastodonteTheme.colorScheme.surface)
                Color(MastodonteTheme.colorScheme.inverseSurface)
                Color(MastodonteTheme.colorScheme.onSurface)
                Color(MastodonteTheme.colorScheme.inverseOnSurface)
                Color(MastodonteTheme.colorScheme.surfaceTint)
            }

            ColorSchemeSection("Surface variant") {
                Color(MastodonteTheme.colorScheme.surfaceVariant)
                Color(MastodonteTheme.colorScheme.onSurfaceVariant)
            }

            ColorSchemeSection("Error") {
                Color(MastodonteTheme.colorScheme.error)
                Color(MastodonteTheme.colorScheme.onError)
            }

            ColorSchemeSection("Error container") {
                Color(MastodonteTheme.colorScheme.errorContainer)
                Color(MastodonteTheme.colorScheme.onErrorContainer)
            }

            ColorSchemeSection("Outline") {
                Color(MastodonteTheme.colorScheme.outline)
            }

            ColorSchemeSection("Outline variant") {
                Color(MastodonteTheme.colorScheme.outlineVariant)
            }

            ColorSchemeSection("Scrim") {
                Color(MastodonteTheme.colorScheme.scrim)
            }
        }
    }
}

/** Preview of [MastodonteTheme]'s [Overlays]. **/
@Composable
@Preview
private fun OverlaysPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            OverlaySection("FAB", MastodonteTheme.overlays.fab)
        }
    }
}

/** Preview of [MastodonteTheme]'s [Shapes]. **/
@Composable
@Preview(heightDp = SHAPES_PREVIEW_HEIGHT)
private fun ShapesPreview() {
    MastodonteTheme {
        Surface(Modifier.fillMaxWidth(), color = MastodonteTheme.colorScheme.background) {
            Column {
                ShapeSection("Extra large", MastodonteTheme.shapes.extraLarge)
                ShapeSection("Large", MastodonteTheme.shapes.large)
                ShapeSection("Medium", MastodonteTheme.shapes.medium)
                ShapeSection("Small", MastodonteTheme.shapes.small)
                ShapeSection("Extra small", MastodonteTheme.shapes.extraSmall)
            }
        }
    }
}

/** Preview of [MastodonteTheme]'s [Spacings]. **/
@Composable
@Preview
private fun SpacingsPreview() {
    MastodonteTheme {
        Surface(Modifier.fillMaxWidth()) {
            Column {
                SpacingSection("Extra large", MastodonteTheme.spacings.extraLarge)
                SpacingSection("Large", MastodonteTheme.spacings.large)
                SpacingSection("Medium", MastodonteTheme.spacings.medium)
                SpacingSection("Small", MastodonteTheme.spacings.small)
                SpacingSection("Extra small", MastodonteTheme.spacings.extraSmall)
            }
        }
    }
}

/** Preview of [MastodonteTheme]'s [Typography]. **/
@Composable
@Preview(heightDp = TYPOGRAPHY_PREVIEW_HEIGHT)
private fun TypographyPreview() {
    MastodonteTheme {
        Surface(Modifier.fillMaxWidth(), color = MastodonteTheme.colorScheme.background) {
            Column {
                TypographySection("Display") {
                    Text("D1", style = MastodonteTheme.typography.displayLarge)
                    Text("D2", style = MastodonteTheme.typography.displayMedium)
                    Text("D3", style = MastodonteTheme.typography.displaySmall)
                }

                TypographySection("Headline") {
                    Text("H1", style = MastodonteTheme.typography.headlineLarge)
                    Text("H2", style = MastodonteTheme.typography.headlineMedium)
                    Text("H3", style = MastodonteTheme.typography.headlineSmall)
                }

                TypographySection("Title") {
                    Text("T1", style = MastodonteTheme.typography.titleLarge)
                    Text("T2", style = MastodonteTheme.typography.titleMedium)
                    Text("T3", style = MastodonteTheme.typography.titleSmall)
                }

                TypographySection("Body") {
                    Text("B1", style = MastodonteTheme.typography.bodyLarge)
                    Text("B2", style = MastodonteTheme.typography.bodyMedium)
                    Text("B3", style = MastodonteTheme.typography.bodySmall)
                }

                TypographySection("Label") {
                    Text("L1", style = MastodonteTheme.typography.labelLarge)
                    Text("L2", style = MastodonteTheme.typography.labelMedium)
                    Text("L3", style = MastodonteTheme.typography.labelSmall)
                }
            }
        }
    }
}

/**
 * [Section] that displays the [Color][com.jeanbarrossilva.mastodonte.platform.theme.Color]s provided in
 * the [content].
 *
 * @param title Text to be shown in the header, that explains what's being displayed.
 * @param modifier [Modifier] to be applied to the underlying [Section].
 * @param content [Color][com.jeanbarrossilva.mastodonte.platform.theme.Color]s to be shown.
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
            Modifier.padding(MastodonteTheme.spacings.large),
            style = MastodonteTheme.typography.titleMedium
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
                .background(MastodonteTheme.colorScheme.primaryContainer)
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
            style = MastodonteTheme.typography.titleMedium
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
            Arrangement.spacedBy(MastodonteTheme.spacings.small),
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
    val padding = PaddingValues(MastodonteTheme.spacings.large)

    Column(modifier) {
        Text(
            title,
            Modifier
                .background(MastodonteTheme.colorScheme.surfaceVariant)
                .padding(padding)
                .fillMaxWidth(),
            MastodonteTheme.colorScheme.onSurfaceVariant,
            style = MastodonteTheme.typography.titleMedium
        )

        content(padding)
    }
}
