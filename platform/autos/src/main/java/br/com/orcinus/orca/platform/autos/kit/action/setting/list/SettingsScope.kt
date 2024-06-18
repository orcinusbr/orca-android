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

package br.com.orcinus.orca.platform.autos.kit.action.setting.list

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import br.com.orcinus.orca.platform.autos.kit.action.setting.ActionScope
import br.com.orcinus.orca.platform.autos.kit.action.setting.Group
import br.com.orcinus.orca.platform.autos.kit.action.setting.Setting

/** Scope through which [Setting]s can be added. */
class SettingsScope internal constructor() {
  /** [Setting]s that have been added. */
  private val mutableSettings = mutableStateListOf<@Composable (Modifier, Shape) -> Unit>()

  /** Immutable [List] with the [Setting]s that have been added. */
  internal val settings
    get() = mutableSettings.toList()

  /**
   * Adds a [Group].
   *
   * @param icon [Icon] that visually represents what it does.
   * @param label Short description of what it's for.
   * @param modifier [Modifier] to be applied to the [Group].
   * @param isInitiallyExpanded Whether it is expanded by default.
   */
  fun group(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isInitiallyExpanded: Boolean = false,
    content: SettingsScope.() -> Unit
  ) {
    with(mutableSettings.size) index@{
      mutableSettings.add { parentModifier, shape ->
        Group(
          icon,
          label,
          parentModifier then modifier,
          isInitiallyExpanded,
          shape as? CornerBasedShape ?: RoundedCornerShape(0),
          content
        )
      }
    }
  }

  /**
   * Adds a [Setting].
   *
   * @param onClick Callback run whenever it's clicked.
   * @param label Short description of what it's for.
   * @param modifier [Modifier] to be applied to the [Setting].
   * @param icon [Icon] that visually represents what it does.
   * @param action Portrays the result of invoking [onClick] or executes a related action.
   */
  fun setting(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    icon: @Composable () -> Unit = {},
    action: ActionScope.() -> Unit = {}
  ) {
    mutableSettings.add { parentModifier, shape ->
      Setting(label, parentModifier then modifier, shape, onClick, icon, action)
    }
  }
}
