package com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.test

import androidx.compose.runtime.Composable
import com.jeanbarrossilva.mastodonte.platform.ui.core.lifecycle.CompleteLifecycleActivity

/** Unmodified subclass of a [CompleteLifecycleActivity]. **/
internal class TestCompleteLifecycleActivity : CompleteLifecycleActivity() {
    @Composable
    @Suppress("TestFunctionName")
    override fun Content() {
    }
}
