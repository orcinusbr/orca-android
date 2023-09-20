package com.jeanbarrossilva.orca.app

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jeanbarrossilva.orca.app.databinding.ActivityOrcaBinding
import com.jeanbarrossilva.orca.app.module.AppModule
import com.jeanbarrossilva.orca.app.module.core.MainCoreModule
import com.jeanbarrossilva.orca.app.module.feature.feed.FeedModule
import com.jeanbarrossilva.orca.app.module.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.search.SearchModule
import com.jeanbarrossilva.orca.app.module.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigatorFactory
import com.jeanbarrossilva.orca.core.auth.AuthenticationLock
import com.jeanbarrossilva.orca.feature.auth.AuthActivity
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.context.loadKoinModules

internal open class OrcaActivity : NavigationActivity(), OnBottomAreaAvailabilityChangeListener {
    private val authenticationLock by inject<AuthenticationLock>()
    private var binding: ActivityOrcaBinding? = null
    private var constraintSet: ConstraintSet? = null

    protected open val coreModule = MainCoreModule()
    protected open val appModule by lazy { AppModule(this) }

    override val height: Int
        get() = binding?.bottomNavigationView?.height ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityOrcaBinding.inflate(layoutInflater)
        constraintSet = ConstraintSet().apply { clone(binding?.root) }
        setContentView(binding?.root)
        inject()
        navigateOnBottomNavigationItemSelection()
        navigateToDefaultDestination()
        lockByNavigatingToAuth()
    }

    override fun onDestroy() {
        super.onDestroy()
        constraintSet = null
        binding = null
    }

    override fun getCurrentOffsetY(): Float {
        return constraintSet
            ?.getConstraint(R.id.bottom_navigation_view)
            ?.transform
            ?.translationY
            ?: 0f
    }

    override fun onBottomAreaAvailabilityChange(offsetY: Float) {
        constraintSet?.apply {
            getConstraint(R.id.container).layout.bottomMargin = -offsetY.toInt()
            getConstraint(R.id.bottom_navigation_view).transform.translationY = offsetY
            applyTo(binding?.root)
        }
    }

    private fun inject() {
        val feedModule = FeedModule(navigator)
        val profileDetailsModule = ProfileDetailsModule(navigator)
        val searchModule = SearchModule(navigator)
        val tootDetailsModule = TootDetailsModule(navigator)
        val modules = listOf(
            appModule,
            coreModule,
            feedModule,
            profileDetailsModule,
            searchModule,
            tootDetailsModule
        )
        loadKoinModules(modules)
    }

    private fun navigateOnBottomNavigationItemSelection() {
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            navigateTo(it.itemId)
            true
        }
    }

    private fun navigateTo(@IdRes itemID: Int) {
        lifecycleScope.launch {
            BottomNavigationItemNavigatorFactory.create(authenticationLock).navigate(
                navigator,
                itemID
            )
        }
    }

    private fun navigateToDefaultDestination() {
        binding?.bottomNavigationView?.selectedItemId = R.id.feed
    }

    private fun lockByNavigatingToAuth() {
        lifecycleScope.launch {
            authenticationLock.requestLock {
                AuthActivity.start(this@OrcaActivity)
            }
        }
    }
}
