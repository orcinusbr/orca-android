package com.jeanbarrossilva.mastodonte.platform.ui.core.composable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme

/**
 * [Fragment] that shows [Composable][Composable] content.
 *
 * @see Content
 **/
abstract class ComposableFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return context?.let {
            ComposeView(it).apply {
                setContent {
                    MastodonteTheme {
                        this@ComposableFragment.Content()
                    }
                }
            }
        }
    }

    /** Content to be shown inside the [ComposeView].  **/
    @Composable
    protected abstract fun Content()
}
