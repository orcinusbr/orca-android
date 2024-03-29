/*
 * Copyright © 2024 Orca
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

package com.jeanbarrossilva.orca.platform.navigation.test.activity

import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import com.jeanbarrossilva.orca.platform.navigation.Navigator
import com.jeanbarrossilva.orca.platform.navigation.navigator

/**
 * Allows for this [FragmentActivity]'s [Navigator] to be obtained by overriding its content and
 * setting it as a [FragmentContainerView].
 *
 * @see navigator
 */
@PublishedApi
internal fun FragmentActivity.makeNavigable() {
  FragmentContainerView(this).apply { id = View.generateViewId() }.run(::setContentView)
}
