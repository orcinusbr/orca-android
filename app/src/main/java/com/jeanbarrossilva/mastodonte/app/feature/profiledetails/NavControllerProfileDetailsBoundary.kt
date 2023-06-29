package com.jeanbarrossilva.mastodonte.app.feature.profiledetails

import android.content.Intent
import android.net.Uri
import androidx.navigation.NavController
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsFragmentDirections
import java.net.URL

internal class NavControllerProfileDetailsBoundary(private val navController: NavController) :
    ProfileDetailsBoundary {
    override fun navigateToWebpage(url: URL) {
        val uri = Uri.parse("$url")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        navController.context.startActivity(intent)
    }

    override fun navigateToTootDetails(id: String) {
        val directions =
            ProfileDetailsFragmentDirections.toTootDetails().apply { arguments.putString(id, "id") }
        navController.navigate(directions)
    }
}
