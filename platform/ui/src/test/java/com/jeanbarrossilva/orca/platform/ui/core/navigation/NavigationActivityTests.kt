package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainerView
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigationActivityTests {
    class NoFragmentContainerViewActivity : NavigationActivity()

    class UnidentifiedFragmentContainerViewActivity : NavigationActivity() {
        var view: FragmentContainerView? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            view = FragmentContainerView(this)
            setContentView(view)
        }

        override fun onDestroy() {
            super.onDestroy()
            view = null
        }
    }

    class EphemeralFragmentContainerViewActivity : NavigationActivity() {
        private var view: FrameLayout? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            view = FrameLayout(this).apply { addView(FragmentContainerView(context)) }
            setContentView(view)
            navigator
        }

        override fun onStart() {
            super.onStart()
            view?.removeAllViews()
        }

        override fun onDestroy() {
            super.onDestroy()
            view = null
        }
    }

    @Test
    fun `GIVEN an Activity without a FragmentContainerView WHEN getting the navigator THEN it throws`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(NoFragmentContainerViewActivity::class.java).setup().use {
            assertThrows(IllegalStateException::class.java) {
                it.get().navigator
            }
        }
    }

    @Test
    fun `GIVEN an Activity with a FragmentContainerView WHEN getting the navigator, removing the FragmentContainerView and getting the navigator again THEN it throws`() { // ktlint-disable max-line-length
        Robolectric.buildActivity(EphemeralFragmentContainerViewActivity::class.java).setup().use {
            assertThrows(IllegalStateException::class.java) {
                it.get().navigator
            }
        }
    }

    @Test
    fun `GIVEN an Activity with an unidentified FragmentContainerView WHEN getting the navigator THEN it's identified`() { // ktlint-disable max-line-length
        Robolectric
            .buildActivity(UnidentifiedFragmentContainerViewActivity::class.java)
            .setup()
            .use {
                it.get().navigator
                assertNotEquals(View.NO_ID, it.get().view?.id)
            }
    }

    @Test
    fun `GIVEN an Activity with a FragmentContainerView WHEN getting the navigator THEN it's obtained`() { // ktlint-disable max-line-length
        Robolectric
            .buildActivity(UnidentifiedFragmentContainerViewActivity::class.java)
            .setup()
            .use { it.get().navigator }
    }
}
