package com.jeanbarrossilva.mastodonte.app

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.jeanbarrossilva.mastodonte.app.databinding.ActivityMastodonteBinding
import com.jeanbarrossilva.mastodonte.app.feature.feed.FeedModule
import com.jeanbarrossilva.mastodonte.app.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.mastodonte.app.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.mastodonte.app.navigation.navigator.BottomNavigationItemNavigatorFactory
import com.jeanbarrossilva.mastodonte.feature.auth.AuthActivity
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import org.koin.core.context.loadKoinModules

internal class MastodonteActivity : AppCompatActivity(), OnBottomAreaAvailabilityChangeListener {
    private var binding: ActivityMastodonteBinding? = null
    private val containerID = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMastodonteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        navigateOnBottomNavigationItemSelection()
        navigateToDefaultDestination()
        navigateToAuth()
        inject()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onBottomAreaAvailabilityChange(isAvailable: Boolean) {
        binding?.bottomNavigationView?.setTonallyElevated(!isAvailable)
    }

    private fun navigateOnBottomNavigationItemSelection() {
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            navigateTo(it.itemId)
            true
        }
    }

    private fun navigateTo(@IdRes itemID: Int) {
        BottomNavigationItemNavigatorFactory.create().navigate(
            supportFragmentManager,
            containerID,
            itemID
        )
    }

    private fun navigateToDefaultDestination() {
        binding?.bottomNavigationView?.selectedItemId = R.id.feed
    }

    private fun navigateToAuth() {
        AuthActivity.start(this)
    }

    private fun inject() {
        val mastodonteModule = MastodonteModule(onBottomAreaAvailabilityChangeListener = this)
        val feedModule = FeedModule(supportFragmentManager, containerID)
        val profileDetailsModule = ProfileDetailsModule(supportFragmentManager, containerID)
        val tootDetailsModule = TootDetailsModule(supportFragmentManager, containerID)
        val modules = listOf(mastodonteModule, feedModule, profileDetailsModule, tootDetailsModule)
        loadKoinModules(modules)
    }
}
