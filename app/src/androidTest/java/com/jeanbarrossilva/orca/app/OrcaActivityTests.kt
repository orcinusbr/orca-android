/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.app

import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.MastodonAuthorizationActivity
import com.jeanbarrossilva.orca.ext.intents.intendStartingOf
import org.junit.Test

internal class OrcaActivityTests {
  @Test
  fun navigatesToAuthorization() {
    intendStartingOf<MastodonAuthorizationActivity> { launchActivity<OrcaActivity>().close() }
  }
}
