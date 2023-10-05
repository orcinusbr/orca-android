package com.jeanbarrossilva.orca.feature.settings.termmuting

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.platform.ui.core.navigation.Navigator
import com.jeanbarrossilva.orca.platform.ui.core.navigation.transition.opening
import com.jeanbarrossilva.orca.std.injector.Injector

class TermMutingFragment internal constructor() : ComposableFragment() {
    private val viewModel by viewModels<TermMutingViewModel> {
        TermMutingViewModel.createFactory(termMuter = Injector.get())
    }

    @Composable
    override fun Content() {
        TermMuting(viewModel, boundary = Injector.get())
    }

    companion object {
        fun navigate(navigator: Navigator) {
            navigator.navigate(opening()) {
                to("settings/term-muting", ::TermMutingFragment)
            }
        }
    }
}
