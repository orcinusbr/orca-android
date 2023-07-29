package com.jeanbarrossilva.mastodonte.app.feature.profiledetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.mastodonte.platform.ui.core.Intent
import java.net.URL

internal class FragmentManagerProfileDetailsBoundary(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) : ProfileDetailsBoundary {
    override fun navigateToWebpage(url: URL) {
        val intent = Intent(url)
        fragmentManager.primaryNavigationFragment?.context?.startActivity(intent)
    }

    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(fragmentManager, containerID, id)
    }
}
