package com.jeanbarrossilva.mastodonte.app.module.feature.feed

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.mastodonte.feature.feed.FeedBoundary
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsFragment

internal class FragmentManagerFeedBoundary(
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) : FeedBoundary {
    override fun navigateToSearch() {
    }

    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(fragmentManager, containerID, id)
    }

    override fun navigateToComposer() {
    }
}
