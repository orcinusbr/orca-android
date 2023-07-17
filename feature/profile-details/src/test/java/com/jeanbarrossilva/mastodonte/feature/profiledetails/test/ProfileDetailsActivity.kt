package com.jeanbarrossilva.mastodonte.feature.profiledetails.test

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.NavGraphBuilder
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.mastodonte.feature.profiledetails.ProfileDetailsFragment
import com.jeanbarrossilva.mastodonte.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodonte.platform.ui.test.core.SingleFragmentActivity
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

internal class ProfileDetailsActivity : SingleFragmentActivity() {
    override val route = "profile-details"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopKoin()
    }

    override fun NavGraphBuilder.add() {
        fragment<ProfileDetailsFragment>()
    }

    private fun inject() {
        val module = ProfileDetailsModule()
        startKoin { modules(module) }
    }

    companion object {
        fun getIntent(
            backwardsNavigationState: BackwardsNavigationState,
            id: String
        ): Intent {
            val context = InstrumentationRegistry.getInstrumentation().context
            val extras = bundleOf(
                ProfileDetailsFragment.BACKWARDS_NAVIGATION_STATE_KEY to backwardsNavigationState,
                ProfileDetailsFragment.ID_KEY to id
            )
            return Intent(context, ProfileDetailsActivity::class.java).apply {
                putExtras(extras)
            }
        }
    }
}
