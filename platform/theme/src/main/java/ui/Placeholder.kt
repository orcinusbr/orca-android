package com.jeanbarrossilva.mastodonte.platform.theme.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

/** Default values of a [Placeholder]. **/
object PlaceholderDefaults {
    /** [Color] by which a [Placeholder] is colored by default. **/
    val color
        @Composable get() = MastodonteTheme.colorScheme.surfaceVariant

    /** [Shape] by which a [Placeholder] is clipped by default. **/
    val shape
        @Composable get() = MastodonteTheme.shapes.medium
}

/**
 * Holds place for loading content.
 *
 * @param modifier [Modifier] to be applied to the underlying [Box].
 * @param shape [Shape] by which the [Placeholder] is clipped.
 * @param color [Color] by which the [Placeholder] is colored.
 **/
@Composable
fun Placeholder(
    modifier: Modifier = Modifier,
    shape: Shape = PlaceholderDefaults.shape,
    color: Color = PlaceholderDefaults.color
) {
    Box(modifier.placeholder(visible = true, color, shape, PlaceholderHighlight.shimmer()))
}

/**
 * Holds place for large, loading [Text].
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 * @param style [TextStyle] for determining the height.
 * @param color [Color] by which the [TextualPlaceholder] is colored.
 **/
@Composable
fun LargeTextualPlaceholder(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = PlaceholderDefaults.color
) {
    TextualPlaceholder(fraction = 1f, modifier, style, color)
}

/**
 * Holds place for medium, loading [Text].
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 * @param style [TextStyle] for determining the height.
 * @param color [Color] by which the [TextualPlaceholder] is colored.
 **/
@Composable
fun MediumTextualPlaceholder(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = PlaceholderDefaults.color
) {
    TextualPlaceholder(fraction = .5f, modifier, style, color)
}

/**
 * Holds place for small, loading [Text].
 *
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 * @param style [TextStyle] for determining the height.
 * @param color [Color] by which the [TextualPlaceholder] is colored.
 **/
@Composable
fun SmallTextualPlaceholder(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = PlaceholderDefaults.color
) {
    TextualPlaceholder(fraction = .2f, modifier, style, color)
}

/**
 * Holds place for loading [Text].
 *
 * @param fraction Available-width-based fraction.
 * @param modifier [Modifier] to be applied to the underlying [Placeholder].
 * @param style [TextStyle] for determining the height.
 * @param color [Color] by which the [TextualPlaceholder] is colored.
 **/
@Composable
private fun TextualPlaceholder(
    fraction: Float,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = PlaceholderDefaults.color
) {
    val height = with(LocalDensity.current) {
        style.fontSize.toDp()
    }

    Box {
        Placeholder(
            modifier
                .requiredHeight(height)
                .fillMaxWidth(fraction),
            shape = when (style) {
                MastodonteTheme.typography.displayLarge,
                MastodonteTheme.typography.displayMedium,
                MastodonteTheme.typography.displaySmall,
                MastodonteTheme.typography.headlineLarge,
                MastodonteTheme.typography.headlineMedium,
                MastodonteTheme.typography.headlineSmall -> MastodonteTheme.shapes.large
                MastodonteTheme.typography.bodySmall,
                MastodonteTheme.typography.labelLarge,
                MastodonteTheme.typography.labelMedium,
                MastodonteTheme.typography.labelSmall -> MastodonteTheme.shapes.small
                else -> PlaceholderDefaults.shape
            },
            color
        )
    }
}

/** Preview of a [Placeholder]. **/
@Composable
@Preview
private fun PlaceholderPreview() {
    MastodonteTheme {
        Placeholder(Modifier.size(128.dp))
    }
}

/** Preview of a [LargeTextualPlaceholder]. **/
@Composable
@Preview
private fun LargeTextualPlaceholderPreview() {
    MastodonteTheme {
        LargeTextualPlaceholder()
    }
}

/** Preview of a [MediumTextualPlaceholder]. **/
@Composable
@Preview
private fun MediumTextualPlaceholderPreview() {
    MastodonteTheme {
        MediumTextualPlaceholder()
    }
}

/** Preview of a [SmallTextualPlaceholder]. **/
@Composable
@Preview
private fun SmallTextualPlaceholderPreview() {
    MastodonteTheme {
        SmallTextualPlaceholder()
    }
}
