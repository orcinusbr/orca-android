/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.feature.feed.test

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import br.com.orcinus.orca.ext.intents.intentOf
import br.com.orcinus.orca.feature.feed.FeedFragment
import br.com.orcinus.orca.platform.testing.activity.SingleFragmentActivity
import br.com.orcinus.orca.platform.testing.context

internal class FeedActivity : SingleFragmentActivity() {
  override val route = "feed"

  override fun NavGraphBuilder.add() {
    fragment<FeedFragment>()
  }

  companion object {
    fun getIntent(userID: String): Intent {
      return intentOf<FeedActivity>(context, FeedFragment.USER_ID_KEY to userID)
    }
  }
}
