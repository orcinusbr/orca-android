/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.platform.navigation

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import br.com.orcinus.orca.platform.testing.activity.scenario.activity
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class NavigatorPoolTests {
  @Test
  fun remembers() {
    launchActivity<NavigationActivity>().use {
      val activity = checkNotNull(it.activity)
      val containerID = Navigator.Pool.getContainerIDOrThrow(activity)
      Navigator.Pool.remember(activity, containerID)
      assertThat(containerID in Navigator.Pool).isTrue()
    }
  }

  @Test
  fun removesNavigatorAfterActivityIsDestroyed() {
    launchActivity<NavigationActivity>().use {
      val activity = checkNotNull(it.activity)
      val containerID = Navigator.Pool.getContainerIDOrThrow(activity)
      Navigator.Pool.remember(activity, containerID)
      it.moveToState(Lifecycle.State.DESTROYED)
      assertThat(containerID in Navigator.Pool).isFalse()
    }
  }
}
