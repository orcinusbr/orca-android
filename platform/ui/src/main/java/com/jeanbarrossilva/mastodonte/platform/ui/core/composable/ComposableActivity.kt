package com.jeanbarrossilva.mastodonte.platform.ui.core.composable

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

/**
 * [Activity] that shows [Composable] content.
 *
 * @see Content
 **/
abstract class ComposableActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MastodonteTheme {
                Content()
            }
        }
    }

    /** Content to be shown inside the [ComposeView]. **/
    @Composable
    protected abstract fun Content()
}
