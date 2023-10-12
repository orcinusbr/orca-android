package com.jeanbarrossilva.orca.platform.ui.core.navigation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentContainerView
import androidx.test.core.app.launchActivity
import org.junit.Assert.assertNotEquals
import org.junit.Test

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

  @Test(expected = IllegalStateException::class)
  fun throwsWhenNavigatorGetterIsCalledOnAnActivityWithoutFragmentContainerView() {
    launchActivity<NoFragmentContainerViewActivity>().use { scenario ->
      scenario.onActivity { activity -> activity.navigator }
    }
  }

  @Test(expected = IllegalStateException::class)
  fun throwsWhenNavigatorGetterIsCalledOnAnActivityWithFragmentContainerViewAndAgainWithoutIt() {
    launchActivity<EphemeralFragmentContainerViewActivity>().use { scenario ->
      scenario.onActivity { activity -> activity.navigator }
    }
  }

  @Test
  fun identifiesFragmentContainerViewIfNotIdentifiedWhenNavigatorGetterIsCalled() {
    launchActivity<UnidentifiedFragmentContainerViewActivity>().use { scenario ->
      scenario.onActivity { activity ->
        activity.navigator
        assertNotEquals(View.NO_ID, activity.view?.id)
      }
    }
  }

  @Test
  fun returnsNavigatorWhenItsGetterIsCalledOnAnActivityWithFragmentContainerView() {
    launchActivity<UnidentifiedFragmentContainerViewActivity>().use { scenario ->
      scenario.onActivity { activity -> activity.navigator }
    }
  }
}
