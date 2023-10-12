package com.jeanbarrossilva.orca.platform.theme

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper

/** [ContextThemeWrapper] with [R.style.Theme_Orca] as its theme. */
internal class OrcaContextThemeWrapper(context: Context) :
  ContextThemeWrapper(context, R.style.Theme_Orca)
