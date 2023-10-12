package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import android.icu.text.CompactDecimalFormat
import java.util.Locale

/** Formats this [Int] so that it is more user-friendly. */
val Int.formatted: String
  get() =
    CompactDecimalFormat.getInstance(Locale.getDefault(), CompactDecimalFormat.CompactStyle.SHORT)
      .format(this)
