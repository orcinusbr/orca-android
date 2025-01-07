/*
 * Copyright © 2024–2025 Orcinus
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

package br.com.orcinus.orca.composite.timeline.composition.interop.spanned.span

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.LocaleList
import android.os.Parcel
import android.text.style.TextAppearanceSpan
import androidx.annotation.ColorInt
import br.com.orcinus.orca.ext.reflection.java.access

/** Creates a [TextAppearanceSpan]. */
internal fun createTextAppearanceSpan(
  family: String?,
  style: Int,
  textSize: Int,
  textColor: ColorStateList,
  textColorLink: ColorStateList,
  textFontWeight: Int,
  textLocales: LocaleList?,
  typeface: Typeface?,
  shadowRadius: Float,
  shadowDx: Float,
  shadowDy: Float,
  @ColorInt shadowColor: Int,
  hasElegantTextHeight: Boolean,
  letterSpacing: Float,
  fontFeatureSettings: String?,
  fontVariationSettings: String?
): TextAppearanceSpan {
  val hasElegantTextHeightAsInt = if (hasElegantTextHeight) 1 else 0
  val hasLetterSpacing = !letterSpacing.isNaN()
  val hasLetterSpacingAsInt = if (hasLetterSpacing) 1 else 0
  val parcel =
    Parcel.obtain().apply parcel@{
      writeString(family)
      writeInt(style)
      writeInt(textSize)
      textColor.writeToParcel(this, ColorStateList.PARCELABLE_WRITE_RETURN_VALUE)
      textColorLink.writeToParcel(this, ColorStateList.PARCELABLE_WRITE_RETURN_VALUE)

      @Suppress("PrivateApi", "SoonBlockedPrivateApi")
      Class.forName("android.graphics.LeakyTypefaceStorage")
        .getDeclaredMethod("writeTypefaceToParcel", Typeface::class.java, Parcel::class.java)
        .access { invoke(null, typeface, this@parcel) }

      writeInt(textFontWeight)
      textLocales?.let { writeParcelable(it, LocaleList.PARCELABLE_WRITE_RETURN_VALUE) }
        ?: writeString(null)
      writeFloat(shadowRadius)
      writeFloat(shadowDx)
      writeFloat(shadowDy)
      writeInt(shadowColor)
      writeInt(hasElegantTextHeightAsInt)
      writeInt(hasElegantTextHeightAsInt)
      writeInt(hasLetterSpacingAsInt)
      writeFloat(letterSpacing)
      writeString(fontFeatureSettings)
      writeString(fontVariationSettings)
      setDataPosition(0)
    }
  return TextAppearanceSpan(parcel).also { parcel.recycle() }
}
