package com.jeanbarrossilva.orca.app

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jeanbarrossilva.orca.app.databinding.ActivityOrcaBinding
import com.jeanbarrossilva.orca.app.module.core.MainCoreModule
import com.jeanbarrossilva.orca.app.module.feature.feed.FeedModule
import com.jeanbarrossilva.orca.app.module.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.search.SearchModule
import com.jeanbarrossilva.orca.app.module.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigatorFactory
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.feature.auth.AuthActivity
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.core.context.loadKoinModules

internal open class OrcaActivity : AppCompatActivity(), OnBottomAreaAvailabilityChangeListener {
    private var binding: ActivityOrcaBinding? = null
    private val containerID = R.id.container

    protected open val coreModule by lazy {
        MainCoreModule(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityOrcaBinding.inflate(layoutInflater)
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
                AuthActivity.start(this@OrcaActivity)
            }
        }
    }

    private fun inject() {
        val feedModule = FeedModule(supportFragmentManager, containerID)
        val profileDetailsModule = ProfileDetailsModule(supportFragmentManager, containerID)
        val searchModule = SearchModule(supportFragmentManager, containerID)
        val tootDetailsModule = TootDetailsModule(supportFragmentManager, containerID)
        val modules =
            listOf(coreModule, feedModule, profileDetailsModule, searchModule, tootDetailsModule)
        loadKoinModules(modules)
    }
}
