package com.jeanbarrossilva.mastodonte.app.module.feature.tootdetails

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsBoundary
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsFragment

internal class FragmentManagerTootDetailsBoundary(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) : TootDetailsBoundary {
    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(fragmentManager, containerID, id)
    }

    override fun pop() {
        fragmentManager.popBackStack()
    }
}
