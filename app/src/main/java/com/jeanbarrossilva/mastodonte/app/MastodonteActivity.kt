package com.jeanbarrossilva.mastodonte.app

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jeanbarrossilva.mastodonte.app.databinding.ActivityMastodonteBinding
import com.jeanbarrossilva.mastodonte.app.module.core.MainCoreModule
import com.jeanbarrossilva.mastodonte.app.module.feature.feed.FeedModule
import com.jeanbarrossilva.mastodonte.app.module.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.mastodonte.app.module.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.mastodonte.app.navigation.navigator.BottomNavigationItemNavigatorFactory
import com.jeanbarrossilva.mastodonte.core.auth.AuthenticationLock
import com.jeanbarrossilva.mastodonte.feature.auth.AuthActivity
import com.jeanbarrossilva.mastodonte.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.context.loadKoinModules

internal open class MastodonteActivity :
    AppCompatActivity(), OnBottomAreaAvailabilityChangeListener {
    private var binding: ActivityMastodonteBinding? = null
    private val containerID = R.id.container

    protected open val coreModule by lazy {
        MainCoreModule(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMastodonteBinding.inflate(layoutInflater)
        inject()
        setContentView(binding?.root)
        navigateOnBottomNavigationItemSelection()
        navigateToDefaultDestination()
        lockByNavigatingToAuth()
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

    private fun lockByNavigatingToAuth() {
        lifecycleScope.launch {
            get<AuthenticationLock>().requestLock {
                AuthActivity.start(this@MastodonteActivity)
            }
        }
    }

    private fun inject() {
        val feedModule = FeedModule(supportFragmentManager, containerID)
        val profileDetailsModule = ProfileDetailsModule(supportFragmentManager, containerID)
        val tootDetailsModule = TootDetailsModule(supportFragmentManager, containerID)
        val modules = listOf(coreModule, feedModule, profileDetailsModule, tootDetailsModule)
        loadKoinModules(modules)
    }
}
