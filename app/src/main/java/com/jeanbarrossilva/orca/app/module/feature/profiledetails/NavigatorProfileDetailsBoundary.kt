package com.jeanbarrossilva.orca.app.module.feature.profiledetails

import android.content.Context
import com.jeanbarrossilva.orca.feature.profiledetails.ProfileDetailsBoundary
import com.jeanbarrossilva.orca.feature.tootdetails.TootDetailsFragment
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import java.net.URL

internal class NavigatorProfileDetailsBoundary(
    private val context: Context,
    private val navigator: Navigator
) : ProfileDetailsBoundary {
    override fun navigateToWebpage(url: URL) {
        val intent = Intent(url)
        context.startActivity(intent)
    }

    override fun navigateToTootDetails(id: String) {
        TootDetailsFragment.navigate(navigator, id)
    }
}
