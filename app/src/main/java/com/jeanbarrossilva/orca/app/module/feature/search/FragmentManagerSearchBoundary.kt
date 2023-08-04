package com.jeanbarrossilva.orca.app.module.feature.search

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.orca.feature.search.SearchBoundary

internal class FragmentManagerSearchBoundary(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) : SearchBoundary {
    override fun navigateToProfileDetails(id: String) {
        ProfileDetailsFragment.navigate(fragmentManager, containerID, id)
    }

    override fun pop() {
        fragmentManager.popBackStack()
    }
}
