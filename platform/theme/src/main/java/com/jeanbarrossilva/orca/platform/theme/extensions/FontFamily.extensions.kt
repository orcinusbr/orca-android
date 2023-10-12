package com.jeanbarrossilva.orca.platform.theme.extensions

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.jeanbarrossilva.orca.platform.theme.R

/** [FontFamily] of the Rubik typeface. */
internal val FontFamily.Companion.Rubik
  get() =
    FontFamily(
      Font(R.font.rubik_light, FontWeight.Light),
      Font(R.font.rubik_light_italic, FontWeight.Light, FontStyle.Italic),
      Font(R.font.rubik_normal),
      Font(R.font.rubik_normal_italic, style = FontStyle.Italic),
      Font(R.font.rubik_medium, FontWeight.Medium),
      Font(R.font.rubik_medium_italic, FontWeight.Medium, FontStyle.Italic),
      Font(R.font.rubik_bold, FontWeight.Bold),
      Font(R.font.rubik_bold_italic, FontWeight.Bold, FontStyle.Italic),
      Font(R.font.rubik_extra_bold, FontWeight.ExtraBold),
      Font(R.font.rubik_extra_bold_italic, FontWeight.ExtraBold, FontStyle.Italic),
      Font(R.font.rubik_black, FontWeight.Black),
      Font(R.font.rubik_black_italic, FontWeight.Black, FontStyle.Italic)
    )
