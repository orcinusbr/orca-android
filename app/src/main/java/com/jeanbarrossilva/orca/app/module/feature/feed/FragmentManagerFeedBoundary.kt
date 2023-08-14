package com.jeanbarrossilva.orca.app.module.feature.feed

import android.content.Context
import com.jeanbarrossilva.orca.feature.composer.ComposerActivity
import com.jeanbarrossilva.orca.feature.feed.FeedBoundary
import com.jeanbarrossilva.orca.feature.search.SearchFragment
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator

internal class FragmentManagerFeedBoundary(
    private val context: Context,
    private val navigator: Navigator
) : FeedBoundary {
    override fun navigateToSearch() {
        SearchFragment.navigate(navigator)
    }

    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(navigator, id)
    }

    override fun navigateToComposer() {
        ComposerActivity.start(context)
    }
}
