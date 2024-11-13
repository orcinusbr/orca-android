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

package br.com.orcinus.orca.app.activity

import android.content.Context
import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isSameInstanceAs
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.feature.profiledetails.ProfileDetailsModule
import br.com.orcinus.orca.platform.testing.activity.scenario.activity
import br.com.orcinus.orca.std.injector.Injector
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class OrcaActivityTests {
  @Test
  fun isInjectedAsContext() =
    launchActivity<SampleOrcaActivity>().use { scenario ->
      assertThat(Injector)
        .transform("get") { injector -> injector.get<Context>() }
        .isSameInstanceAs(scenario.activity)
    }

  @Test
  fun registersCoreModule() =
    launchActivity<SampleOrcaActivity>().use { scenario ->
      assertThat(Injector)
        .transform("from") { injector -> injector.from<CoreModule>() }
        .isSameInstanceAs(scenario.activity?.coreModule)
    }

  @Test
  fun registersProfileDetailsModule() =
    launchActivity<SampleOrcaActivity>().use { scenario ->
      assertThat(Injector)
        .transform("from") { injector -> injector.from<ProfileDetailsModule>() }
        .isSameInstanceAs(scenario.activity?.profileDetailsModule)
    }
}
