package com.jeanbarrossilva.orca.feature.feed

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.feature.feed.viewmodel.FeedViewModel
import com.jeanbarrossilva.orca.platform.ui.core.argument
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.context.ContextProvider
import com.jeanbarrossilva.orca.std.injector.Injector

class FeedFragment internal constructor() : ComposableFragment(), ContextProvider {
    private val userID by argument<String>(USER_ID_KEY)
    private val viewModel by viewModels<FeedViewModel> {
        FeedViewModel.createFactory(
            contextProvider = this,
            feedProvider = Injector.from<FeedModule>().get(),
            tootProvider = Injector.from<FeedModule>().get(),
            userID
        )
    }

    constructor(userID: String) : this() {
        arguments = bundleOf(USER_ID_KEY to userID)
    }

    @Composable
    override fun Content() {
        Feed(
            viewModel,
            boundary = Injector.from<FeedModule>().get(),
            onBottomAreaAvailabilityChangeListener = Injector.get()
        )
    }

    override fun provide(): Context {
        return requireContext()
    }

    companion object {
        internal const val USER_ID_KEY = "user-id"
    }
}
