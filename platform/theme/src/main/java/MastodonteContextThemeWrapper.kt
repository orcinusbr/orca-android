package com.jeanbarrossilva.mastodonte.platform.theme

import android.content.Context
import androidx.appcompat.view.ContextThemeWrapper

/** [ContextThemeWrapper] with [R.style.Theme_Mastodonte] as its theme. **/
internal class MastodonteContextThemeWrapper(context: Context) :
    ContextThemeWrapper(context, R.style.Theme_Mastodonte)
