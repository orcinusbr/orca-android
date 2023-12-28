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

package com.jeanbarrossilva.orca.feature.feed.test

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import com.jeanbarrossilva.orca.feature.feed.FeedFragment
import com.jeanbarrossilva.orca.platform.testing.context
import com.jeanbarrossilva.orca.platform.ui.core.Intent
import com.jeanbarrossilva.orca.platform.ui.test.core.SingleFragmentActivity

internal class FeedActivity : SingleFragmentActivity() {
  override val route = "feed"

  override fun NavGraphBuilder.add() {
    fragment<FeedFragment>()
  }

  companion object {
    fun getIntent(userID: String): Intent {
      return Intent<FeedActivity>(context, FeedFragment.USER_ID_KEY to userID)
    }
  }
}
