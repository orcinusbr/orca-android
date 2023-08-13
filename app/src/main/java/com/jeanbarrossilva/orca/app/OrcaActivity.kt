package com.jeanbarrossilva.orca.app

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.jeanbarrossilva.orca.app.databinding.ActivityOrcaBinding
import com.jeanbarrossilva.orca.app.module.AppModule
import com.jeanbarrossilva.orca.app.module.core.MainCoreModule
import com.jeanbarrossilva.orca.app.module.feature.composer.ComposerModule
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
    private var constraintSet: ConstraintSet? = null
    private val containerID = R.id.container

    protected open val coreModule = MainCoreModule()
    protected open val appModule by lazy { AppModule(this) }

    override val height: Int
        get() = binding?.bottomNavigationView?.height ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityOrcaBinding.inflate(layoutInflater)
        constraintSet = ConstraintSet().apply { clone(binding?.root) }
        inject()
        setContentView(binding?.root)
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
            connect(
                R.id.bottom_navigation_card,
                ConstraintSet.TOP,
                R.id.bottom_navigation_view,
                ConstraintSet.TOP,
                offsetY.toInt()
            )
            applyTo(binding?.root)
        }
    }

    private fun inject() {
        val composerModule = ComposerModule(supportFragmentManager)
        val feedModule = FeedModule(supportFragmentManager, containerID)
        val profileDetailsModule = ProfileDetailsModule(supportFragmentManager, containerID)
        val searchModule = SearchModule(supportFragmentManager, containerID)
        val tootDetailsModule = TootDetailsModule(supportFragmentManager, containerID)
        val modules = listOf(
            appModule,
            composerModule,
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
}
