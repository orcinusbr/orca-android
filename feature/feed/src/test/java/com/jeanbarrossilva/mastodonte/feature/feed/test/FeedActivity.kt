package com.jeanbarrossilva.mastodonte.feature.feed.test

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.mastodonte.feature.feed.FeedFragment
import com.jeanbarrossilva.mastodonte.platform.ui.core.Intent
import com.jeanbarrossilva.mastodonte.platform.ui.test.core.SingleFragmentActivity
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

internal class FeedActivity : SingleFragmentActivity() {
    override val route = "feed"

    override fun NavGraphBuilder.add() {
        fragment<FeedFragment>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    private fun inject() {
        val module = FeedModule()
        startKoin { modules(module) }
    }

    companion object {
        fun getIntent(userID: String): Intent {
            val context = InstrumentationRegistry.getInstrumentation().context
            return Intent<FeedActivity>(context, FeedFragment.USER_ID_KEY to userID)
        }
    }
}
