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

package br.com.orcinus.orca.platform.markdown.annotated

import assertk.Assert
import assertk.assertions.support.expected
import assertk.assertions.support.expectedListDiff
import assertk.assertions.support.show
import br.com.orcinus.orca.platform.markdown.spanned.span.areStructurallyEqual
import br.com.orcinus.orca.platform.testing.context

/**
 * Asserts that each of the spans on which assertion is taking place are structurally equal to the
 * ones that have been specified (in the order in which they were given, and according to
 * [Any.areStructurallyEqual]).
 *
 * @param expected Spans to be structurally compared to the ones being asserted against.
 */
internal fun <T : Any> Assert<Array<out T>>.areStructurallyEqual(
  vararg expected: Any
): Assert<Array<out T>> {
  given { actual ->
    if (actual.size != expected.size) {
      expectedListDiff(expected.toList(), actual.toList())
    } else {
      actual.forEachIndexed { index, span ->
        if (!span.areStructurallyEqual(context, expected[index])) {
          expected(
            "${show(span)} to be structurally equal to ${show(expected[index])} but it is not."
          )
        }
      }
    }
  }
  return this
}
