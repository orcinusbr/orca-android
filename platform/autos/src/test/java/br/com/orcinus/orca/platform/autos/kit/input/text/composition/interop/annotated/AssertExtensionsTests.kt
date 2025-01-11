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

package br.com.orcinus.orca.platform.autos.kit.input.text.composition.interop.annotated

import android.graphics.Typeface
import android.text.style.AbsoluteSizeSpan
import android.text.style.StyleSpan
import assertk.assertThat
import kotlin.test.Test
import org.junit.runner.RunWith
import org.opentest4j.AssertionFailedError
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AssertExtensionsTests {
  @Test
  fun passesWhenSpansAreStructurallyEqual() {
    assertThat(arrayOf(StyleSpan(Typeface.ITALIC), AbsoluteSizeSpan(2, true)))
      .areStructurallyEqual(StyleSpan(Typeface.ITALIC), AbsoluteSizeSpan(2, true))
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenSpansAreOrderedUnexpectedly() {
    assertThat(arrayOf(AbsoluteSizeSpan(2, true), StyleSpan(Typeface.ITALIC)))
      .areStructurallyEqual(StyleSpan(Typeface.ITALIC), AbsoluteSizeSpan(2, true))
  }

  @Test(expected = AssertionFailedError::class)
  fun failsWhenSpansAreStructurallyDistinct() {
    assertThat(arrayOf(StyleSpan(Typeface.NORMAL))).areStructurallyEqual(StyleSpan(Typeface.BOLD))
  }
}
