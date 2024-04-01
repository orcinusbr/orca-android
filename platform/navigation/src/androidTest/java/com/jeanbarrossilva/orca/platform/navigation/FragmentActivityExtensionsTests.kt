/*
 * Copyright © 2023-2024 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.navigation

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isNotEqualTo
import org.junit.Test

internal class FragmentActivityExtensionsTests {
  class NoFragmentContainerViewActivity : FragmentActivity()

  class UnidentifiedFragmentContainerViewActivity : FragmentActivity() {
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

  class EphemeralFragmentContainerViewActivity : FragmentActivity() {
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
        assertThat(activity.view?.id).isNotEqualTo(View.NO_ID)
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
