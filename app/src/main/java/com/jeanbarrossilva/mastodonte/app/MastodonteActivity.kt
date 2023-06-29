package com.jeanbarrossilva.mastodonte.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jeanbarrossilva.mastodonte.app.databinding.ActivityMastodonteBinding
import com.jeanbarrossilva.mastodonte.app.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.mastodonte.app.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.context.loadKoinModules

internal class MastodonteActivity : AppCompatActivity(), OnBottomAreaAvailabilityChangeListener {
    private var binding: ActivityMastodonteBinding? = null

    private val navController
        get() = supportFragmentManager
            .findFragmentById(R.id.container_view)
            .let { it as NavHostFragment }
            .navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMastodonteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.bottomNavigationView?.setupWithNavController(navController)
        inject()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onBottomAreaAvailabilityChange(isAvailable: Boolean) {
        binding?.bottomNavigationView?.setTonallyElevated(!isAvailable)
    }

    private fun inject() {
        val mastodonteModule =
            MastodonteModule(navController, onBottomAreaAvailabilityChangeListener = this)
        val profileDetailsModule = ProfileDetailsModule()
        val tootDetailsModule = TootDetailsModule()
        val modules = listOf(mastodonteModule, profileDetailsModule, tootDetailsModule)
        loadKoinModules(modules)
    }
}
