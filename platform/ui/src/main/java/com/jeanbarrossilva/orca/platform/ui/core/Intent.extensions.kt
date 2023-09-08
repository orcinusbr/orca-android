package com.jeanbarrossilva.orca.platform.ui.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Puts [extras] into this [Intent] if it isn't `null`.
 *
 * @param extras Extras [Bundle] to be put.
 **/
@PublishedApi
internal fun Intent.putExtras(extras: Bundle?): Intent {
    return extras?.let(::putExtras) ?: this
}

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

/**
 * [Intent] that allows the user to share the [text] externally.
 *
 * @param text Content to be shared.
 **/
fun Intent(text: String): Intent {
    return Intent(Intent.ACTION_SEND).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
}
