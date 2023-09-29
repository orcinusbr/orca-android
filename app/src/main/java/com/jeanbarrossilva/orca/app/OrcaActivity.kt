package com.jeanbarrossilva.orca.app

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jeanbarrossilva.orca.app.databinding.ActivityOrcaBinding
import com.jeanbarrossilva.orca.app.module.AppModule
import com.jeanbarrossilva.orca.app.module.core.CoreModule
import com.jeanbarrossilva.orca.app.module.core.MainCoreModule
import com.jeanbarrossilva.orca.app.module.feature.feed.FeedModule
import com.jeanbarrossilva.orca.app.module.feature.profiledetails.ProfileDetailsModule
import com.jeanbarrossilva.orca.app.module.feature.search.SearchModule
import com.jeanbarrossilva.orca.app.module.feature.tootdetails.TootDetailsModule
import com.jeanbarrossilva.orca.app.navigation.navigator.BottomNavigationItemNavigatorFactory
import com.jeanbarrossilva.orca.core.auth.SomeAuthenticationLock
import com.jeanbarrossilva.orca.platform.theme.reactivity.OnBottomAreaAvailabilityChangeListener
import com.jeanbarrossilva.orca.platform.ui.core.navigation.NavigationActivity
import com.jeanbarrossilva.orca.std.injector.Injector
import kotlinx.coroutines.launch

internal open class OrcaActivity : NavigationActivity(), OnBottomAreaAvailabilityChangeListener {
    private var binding: ActivityOrcaBinding? = null
    private var constraintSet: ConstraintSet? = null

    protected open val coreModule: CoreModule by lazy { MainCoreModule() }
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
    }

    override fun onDestroy() {
        super.onDestroy()
        constraintSet = null
        binding = null
        Injector.clear()
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
        appModule.inject()
        coreModule.inject()
        FeedModule(this).inject()
        ProfileDetailsModule(this).inject()
        SearchModule(navigator).inject()
        TootDetailsModule.inject(this)
    }

    private fun navigateOnBottomNavigationItemSelection() {
        binding?.bottomNavigationView?.setOnItemSelectedListener {
            navigateTo(it.itemId)
            true
        }
    }

    private fun navigateTo(@IdRes itemID: Int) {
        lifecycleScope.launch {
            Injector.get<SomeAuthenticationLock>().requestUnlock {
                BottomNavigationItemNavigatorFactory.create(it).navigate(navigator, itemID)
            }
        }
    }

    private fun navigateToDefaultDestination() {
        binding?.bottomNavigationView?.selectedItemId = R.id.feed
    }
}
