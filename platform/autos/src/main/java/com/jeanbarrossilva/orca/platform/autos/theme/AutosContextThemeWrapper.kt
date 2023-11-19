package com.jeanbarrossilva.orca.platform.autos.theme

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper
import com.jeanbarrossilva.orca.platform.autos.R

/** [ContextThemeWrapper] with [R.style.Theme_Autos] as its theme. */
internal class AutosContextThemeWrapper(context: Context) :
  ContextThemeWrapper(context, R.style.Theme_Autos)
