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

package br.com.orcinus.orca.app.activity.masking.scope

import android.content.pm.ActivityInfo
import androidx.test.core.app.launchActivity
import br.com.orcinus.orca.app.activity.masking.MaskableFrameLayout
import br.com.orcinus.orca.platform.testing.context
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import org.robolectric.Shadows.shadowOf

/**
 * Runs a test targeting the masking of a [MaskableFrameLayout].
 *
 * @param body Lambda in which both the masking and assertion on the resulted state are to be
 *   performed.
 */
@OptIn(ExperimentalContracts::class)
internal fun runMaskerTest(body: MaskerScope.() -> Unit) {
  contract { callsInPlace(body, InvocationKind.EXACTLY_ONCE) }
  val activityInfo =
    ActivityInfo().apply {
      name = MaskerScope.MaskingActivity::class.java.name
      packageName = context.packageName
    }
  shadowOf(context.packageManager).addOrUpdateActivity(activityInfo)
  launchActivity<MaskerScope.MaskingActivity>().use { scenario ->
    scenario.onActivity { activity: MaskerScope.MaskingActivity ->
      val view = MaskableFrameLayout(activity)
      MaskerScope(view).body()
    }
  }
}
