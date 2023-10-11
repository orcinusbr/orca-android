package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.std.injector.Injector

class TermMutingFragment internal constructor() : ComposableFragment() {
    private val module by lazy { Injector.from<TermMutingModule>() }
    private val viewModel by viewModels<TermMutingViewModel> {
        TermMutingViewModel.createFactory(module.termMuter())
    }

    @Composable
    override fun Content() {
        TermMuting(viewModel, module.boundary())
    }

    companion object {
        fun navigate(navigator: Navigator) {
            navigator.navigate(opening()) {
                to("settings/term-muting", ::TermMutingFragment)
            }
        }
    }
}
