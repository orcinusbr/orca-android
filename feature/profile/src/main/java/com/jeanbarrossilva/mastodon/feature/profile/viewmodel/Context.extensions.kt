package com.jeanbarrossilva.mastodon.feature.profile.viewmodel

import android.content.Context
import android.content.Intent

/**
 * Opens the share sheet so that the [text] can be shared.
 *
 * @param text Text to be shared.
 **/
internal fun Context.share(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(intent)
}
