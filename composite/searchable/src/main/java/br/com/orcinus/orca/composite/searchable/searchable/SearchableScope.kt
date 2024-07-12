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

package br.com.orcinus.orca.composite.searchable.searchable

import br.com.orcinus.orca.platform.autos.kit.input.text.SearchTextField

/** Scope from which a [SearchTextField] can be either shown or dismissed. */
abstract class SearchableScope internal constructor() {
  /** Shows the [SearchTextField]. */
  abstract fun show()

  /** Dismisses the [SearchTextField]. */
  internal abstract fun dismiss()
}
