package com.jeanbarrossilva.mastodonte.app.feature.profile

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsNavigator
import com.jeanbarrossilva.mastodonte.app.feature.destinations.TootDetailsDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.lang.ref.WeakReference
import java.net.URL

internal class DefaultProfileDetailsNavigator private constructor(
    private val contextRef: WeakReference<Context>,
    private val destinationsNavigator: DestinationsNavigator
) : ProfileDetailsNavigator {
    constructor(context: Context, destinationsNavigator: DestinationsNavigator) :
        this(WeakReference(context), destinationsNavigator)

    override fun navigateToWebpage(url: URL) {
        val context = contextRef.get() ?: return
        val uri = Uri.parse("$url")
        val intent =
            Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        context.startActivity(intent)
    }

    override fun navigateToTootDetails(id: String) {
        val destination = TootDetailsDestination(id)
        destinationsNavigator.navigate(destination)
    }
}
