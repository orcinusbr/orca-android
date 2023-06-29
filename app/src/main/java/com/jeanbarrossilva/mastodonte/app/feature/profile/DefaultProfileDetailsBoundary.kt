package com.jeanbarrossilva.mastodonte.app.feature.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsBoundary
import java.lang.ref.WeakReference
import java.net.URL

internal class DefaultProfileDetailsBoundary private constructor(
    private val contextRef: WeakReference<Context>
) : ProfileDetailsBoundary {
    constructor(context: Context) : this(WeakReference(context))

    override fun navigateToWebpage(url: URL) {
        val context = contextRef.get() ?: return
        val uri = Uri.parse("$url")
        val intent =
            Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        context.startActivity(intent)
    }

    override fun navigateToTootDetails(id: String) {
    }
}
