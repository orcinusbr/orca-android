package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

/**
 * Creates a [Part.Spanned] with the given [span].
 *
 * @param span Span applied to the [Part.indices].
 */
internal operator fun Part.plus(span: Any): Part.Spanned {
  return Part.Spanned(indices, span)
}
