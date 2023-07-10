package com.jeanbarrossilva.mastodon.feature.profiledetails.test

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.mastodon.feature.profiledetails.navigation.BackwardsNavigationState
import com.jeanbarrossilva.mastodonte.core.profile.Profile

/**
 * [ActivityScenarioRule] that launches a [ProfileDetailsActivity].
 *
 * @param backwardsNavigationState Defines the availability of backwards navigation.
 * @param id ID of the [Profile].
 **/
@Suppress("TestFunctionName")
internal fun ProfileDetailsActivityScenarioRule(
    backwardsNavigationState: BackwardsNavigationState,
    id: String
): ActivityScenarioRule<ProfileDetailsActivity> {
    val context = InstrumentationRegistry.getInstrumentation().context
    val intent = ProfileDetailsActivity.getIntent(context, backwardsNavigationState, id)
    return ActivityScenarioRule(intent)
}
