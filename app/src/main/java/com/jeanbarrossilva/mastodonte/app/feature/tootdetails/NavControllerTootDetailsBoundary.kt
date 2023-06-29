package com.jeanbarrossilva.mastodonte.app.feature.tootdetails

import androidx.core.os.bundleOf
import androidx.navigation.NavController
import com.jeanbarrossilva.mastodonte.feature.tootdetails.TootDetailsBoundary

internal class NavControllerTootDetailsBoundary(private val navController: NavController) :
    TootDetailsBoundary {
    override fun navigateToTootDetails(id: String) {
        navController.navigate(
            com.jeanbarrossilva.mastodonte.feature.tootdetails.R.id.toot_details_fragment,
            bundleOf(id to "id")
        )
    }

    override fun pop() {
        navController.popBackStack()
    }
}
