package com.jeanbarrossilva.mastodonte.platform.theme.extensions

import androidx.compose.foundation.layout.height
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.isSpecified
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

/**
 * [Modifier] provider for styling UI placeholders.
 *
 * @see modifier
 **/
abstract class Placeholder protected constructor() {
    /** Height to be set to the [modifier]. **/
    @get:Composable
    protected abstract val height: Dp

    /**
     * [Modifier] to be applied to the place-held [Composable]. Has no effect if the [withHeightOf] is
     * [unspecified][Dp.Unspecified].
     **/
    internal val modifier = Modifier.`if`({ height.isSpecified }) { height(height) }

    /**
     * [Placeholder] that defines its [height] according to the [TextStyle]'s
     * [font size][TextStyle.fontSize].
     *
     * @param textStyle Provides the [TextStyle] that'll determine the height of this [Placeholder].
     * Defaults to the current one.
     **/
    data class Text(
        private val textStyle: @Composable () -> TextStyle = { LocalTextStyle.current }
    ) : Placeholder() {
        override val height
            @Composable get() = with(LocalDensity.current) { textStyle().fontSize.toDp() }
    }

    companion object {
        /** Default [Color] of a place-held [Composable]. **/
        internal val defaultColor
            @Composable get() = MastodonteTheme.colorScheme.surfaceVariant

        /** Default [Shape] of a place-held [Composable]. **/
        internal val defaultShape
            @Composable get() = MastodonteTheme.shapes.medium

        /**
         * Creates a [Placeholder] whose [height][Placeholder.withHeightOf] is equal to the given one.
         *
         * @param height [Placeholder]'s [height][Placeholder.withHeightOf].
         **/
        infix fun withHeightOf(height: Dp): Placeholder {
            return object : Placeholder() {
                override val height
                    @Composable get() = height
            }
        }
    }
}

/**
 * Returns the result of the given [transform] if the [condition] is `true`; otherwise, returns the
 * receiver [Modifier].
 *
 * @param condition Determines whether the result of [transform] will get returned.
 * @param transform Transformation to be made to this [Modifier].
 **/
fun Modifier.`if`(
    condition: @Composable Modifier.() -> Boolean,
    transform: @Composable Modifier.() -> Modifier
): Modifier {
    @Suppress("UnnecessaryComposedModifier")
    return composed {
        if (condition()) transform() else this
    }
}

/**
 * Indicates that the content is being loaded.
 *
 * @param placeholder [Placeholder] to be applied.
 * @param shape [Shape] by which the [placeholder] will be clipped.
 * @param isVisible Whether the [placeholder] is visible.
 **/
fun Modifier.placeholder(placeholder: Placeholder, shape: Shape, isVisible: Boolean): Modifier {
    return composed {
        placeholder(placeholder, Placeholder.defaultColor, shape, isVisible)
    }
}

/**
 * Indicates that the content is being loaded.
 *
 * @param placeholder [Placeholder] to be applied.
 * @param isVisible Whether the [placeholder] is visible.
 **/
fun Modifier.placeholder(placeholder: Placeholder, isVisible: Boolean): Modifier {
    return composed {
        placeholder(placeholder, Placeholder.defaultColor, Placeholder.defaultShape, isVisible)
    }
}

/**
 * Indicates that the content is being loaded.
 *
 * @param placeholder [Placeholder] to be applied.
 * @param shape [Shape] by which the [placeholder] will be clipped.
 * @param isVisible Whether the [placeholder] is visible.
 * @param color [Color] to color the [placeholder] with.
 **/
fun Modifier.placeholder(
    placeholder: Placeholder,
    color: Color,
    shape: Shape,
    isVisible: Boolean
): Modifier {
    return composed {
        placeholder(isVisible, color, shape, PlaceholderHighlight.shimmer()).`if`({ isVisible }) {
            then(placeholder.modifier)
        }
    }
}
