package com.jeanbarrossilva.orca.feature.feed.test

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.feature.feed.FeedFragment
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity

internal class FeedActivity : SingleFragmentActivity() {
    override val route = "feed"

    override fun NavGraphBuilder.add() {
        fragment<FeedFragment>()
    }

    companion object {
        fun getIntent(userID: String): Intent {
            val context = InstrumentationRegistry.getInstrumentation().context
            return Intent<FeedActivity>(context, FeedFragment.USER_ID_KEY to userID)
        }
    }
}
