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

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView

/**
 * [Navigator] through which [Fragment] navigation can be performed.
 *
 * **NOTE**: Because the [FragmentContainerView] that this [FragmentActivity] holds needs to have an
 * ID for the [Navigator] to work properly, one is automatically generated and assigned to it if it
 * doesn't already have one.
 *
 * @throws IllegalStateException If a [FragmentContainerView] is not found within the [View] tree.
 */
val FragmentActivity.navigator
  @Throws(IllegalStateException::class) get() = Navigator.Pool.get(this)