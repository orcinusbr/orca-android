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

package br.com.orcinus.orca.core.mastodon.instance.requester

import br.com.orcinus.orca.std.visibility.PackageProtected

/**
 * Denotes that an API is an internal [Requester] structure and shouldn't be referenced by external
 * ones.
 */
@PackageProtected("Such API is intended to be referenced only by internal `Requester` structures.")
internal annotation class InternalRequesterApi
