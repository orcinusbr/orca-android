package com.jeanbarrossilva.orca.feature.settings

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import com.jeanbarrossilva.orca.platform.ui.core.composable.ComposableFragment
import com.jeanbarrossilva.orca.std.injector.Injector

class SettingsFragment : ComposableFragment() {
    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.createFactory(termMuter = Injector.get())
    }

    @Composable
    override fun Content() {
        Settings(viewModel, boundary = Injector.get())
    }
}
