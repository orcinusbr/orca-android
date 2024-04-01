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

package com.jeanbarrossilva.orca.platform.navigation

import androidx.test.core.app.launchActivity
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.jeanbarrossilva.orca.ext.intents.intentOf
import com.jeanbarrossilva.orca.platform.testing.context
import org.junit.Test

internal class ActivityExtensionsTests {
  @Test
  fun getsIntentExtra() {
    launchActivity<NavigationActivity>(intentOf<NavigationActivity>(context, "extra" to 0)).use {
      scenario ->
      scenario.onActivity { activity ->
        assertThat(activity.extra<Int>("extra").value).isEqualTo(0)
      }
    }
  }
}
