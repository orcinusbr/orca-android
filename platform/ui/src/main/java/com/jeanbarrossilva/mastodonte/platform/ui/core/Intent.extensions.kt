package com.jeanbarrossilva.mastodonte.platform.ui.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf

/**
 * [Intent] through which the [Activity] can be started.
 *
 * @param context [Context] to create the [Intent] with.
 * @param args Arguments to be passed to the [Intent]'s [extras][Intent.getExtras].
 * @see Context.startActivity
 **/
inline fun <reified T : Activity> Intent(
    context: Context,
    vararg args: Pair<String, Any?>
): Intent {
    val extras = bundleOf(*args)
    return Intent(context, T::class.java).apply { putExtras(extras) }
}
