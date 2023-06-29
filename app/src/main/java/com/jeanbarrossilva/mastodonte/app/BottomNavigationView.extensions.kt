package com.jeanbarrossilva.mastodonte.app

import android.content.Context
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.ui.unit.Density
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Changes the [elevation] based on [BottomAppBar]'s [BottomAppBarDefaults.ContainerElevation].
 *
 * @param isTonallyElevated Whether this [BottomNavigationView] should be tonally-elevated.
 **/
internal fun BottomNavigationView.setTonallyElevated(isTonallyElevated: Boolean) {
    elevation = if (isTonallyElevated) calculateTonalElevation(context) else 0f
}

/**
 * Calculates what the value of [BottomAppBarDefaults.ContainerElevation] is in pixels.
 *
 * @param context [Context] with which the [Density] that handles the calculation will be
 * instantiated.
 **/
private fun calculateTonalElevation(context: Context): Float {
    val density = Density(context)
    return with(density) { BottomAppBarDefaults.ContainerElevation.toPx() }
}
