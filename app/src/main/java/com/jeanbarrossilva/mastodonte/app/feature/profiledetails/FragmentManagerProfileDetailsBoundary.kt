package com.jeanbarrossilva.mastodonte.app.feature.profiledetails

import android.content.Intent
import android.net.Uri
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodon.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsFragment
import java.net.URL

internal class FragmentManagerProfileDetailsBoundary(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) : ProfileDetailsBoundary {
    override fun navigateToWebpage(url: URL) {
        val uri = Uri.parse("$url")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }
        fragmentManager.primaryNavigationFragment?.context?.startActivity(intent)
    }

    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(fragmentManager, containerID, id)
    }
}
