package com.jeanbarrossilva.orca.app.module.feature.feed

import android.content.Context
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import com.jeanbarrossilva.orca.feature.composer.ComposerActivity
import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.feature.search.SearchFragment
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment

internal class FragmentManagerFeedBoundary(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    @IdRes private val containerID: Int
) : FeedBoundary {
    override fun navigateToSearch() {
        SearchFragment.navigate(fragmentManager, containerID)
    }

    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(fragmentManager, containerID, id)
    }

    override fun navigateToComposer() {
        ComposerActivity.start(context)
    }
}
