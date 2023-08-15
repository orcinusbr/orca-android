package com.jeanbarrossilva.orca.feature.feed

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.core.feed.FeedProvider
import com.jeanbarrossilva.orca.core.feed.profile.toot.TootProvider
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject

class FeedFragment internal constructor() : ComposableFragment(), ContextProvider {
    private val feedProvider by inject<FeedProvider>()
    private val tootProvider by inject<TootProvider>()
    private val userID by argument<String>(USER_ID_KEY)
    private val viewModel by viewModels<FeedViewModel> {
        FeedViewModel.createFactory(contextProvider = this, feedProvider, tootProvider, userID)
    }
    private val boundary by inject<FeedBoundary>()

    constructor(userID: String) : this() {
        arguments = bundleOf(USER_ID_KEY to userID)
    }

    @Composable
    override fun Content() {
        Feed(viewModel, boundary, onBottomAreaAvailabilityChangeListener = get())
    }

    override fun provide(): Context {
        return requireContext()
    }

    companion object {
        internal const val USER_ID_KEY = "user-id"
    }
}
