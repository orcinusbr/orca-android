/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.autos.kit.input.text.markdown.annotated

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import br.com.orcinus.orca.autos.colors.Colors
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.std.markdown.Markdown
import br.com.orcinus.orca.std.markdown.style.Style
import kotlin.reflect.KClass

/** Converts this [Markdown] into an [AnnotatedString]. */
@Composable
fun Markdown.toAnnotatedString(): AnnotatedString {
  return toAnnotatedString(AutosTheme.colors)
}

/**
 * Converts this [Markdown] into an [AnnotatedString].
 *
 * @param colors [Colors] by which the [AnnotatedString] can be colored.
 */
fun Markdown.toAnnotatedString(colors: Colors): AnnotatedString {
  val conversions = HashMap<KClass<Style>, SpanStyle>()
  return buildAnnotatedString {
    append(this@toAnnotatedString)
    styles.forEach {
      addStyle(
        @Suppress("UNCHECKED_CAST")
        conversions.getOrPut(it::class as KClass<Style>) { it.toSpanStyle(colors) },
        it.indices.first,
        it.indices.last.inc()
      )
    }
  }
}
