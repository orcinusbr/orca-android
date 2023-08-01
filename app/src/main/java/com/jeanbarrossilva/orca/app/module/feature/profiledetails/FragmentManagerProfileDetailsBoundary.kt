package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.Intent
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
