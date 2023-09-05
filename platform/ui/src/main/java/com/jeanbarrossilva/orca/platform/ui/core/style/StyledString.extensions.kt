package com.jeanbarrossilva.orca.platform.ui.core.style

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.std.styledstring.Style
import com.jeanbarrossilva.orca.std.styledstring.StyledString

/** Converts this [StyledString] into an [AnnotatedString]. **/
@Composable
fun StyledString.toAnnotatedString(): AnnotatedString {
    return toAnnotatedString(OrcaTheme.colors)
}

/**
 * Converts this [StyledString] into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [AnnotatedString] can be colored.
 **/
fun StyledString.toAnnotatedString(colors: Colors): AnnotatedString {
    val conversions = HashMap<Style, SpanStyle>()
    return buildAnnotatedString {
        append(this@toAnnotatedString)
        styles.forEach {
            addStyle(
                conversions.getOrPut(it) { it.toSpanStyle(colors) },
                it.indices.first,
                it.indices.last.inc()
            )
        }
    }
}
