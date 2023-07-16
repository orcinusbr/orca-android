package com.jeanbarrossilva.mastodonte.feature.profiledetails.test

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.mastodonte.feature.profiledetails.navigation.BackwardsNavigationState

/**
 * Launches a [ProfileDetailsActivity].
 **/
internal fun launchProfileDetailsActivity(
    backwardsNavigationState: BackwardsNavigationState,
    id: String
): ActivityScenario<ProfileDetailsActivity> {
    val context = InstrumentationRegistry.getInstrumentation().context
    val intent = ProfileDetailsActivity.getIntent(context, backwardsNavigationState, id)
    return launchActivity(intent)
}
