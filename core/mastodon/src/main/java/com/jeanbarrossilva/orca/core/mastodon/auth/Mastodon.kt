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

package com.jeanbarrossilva.orca.core.mastodon.auth

import com.jeanbarrossilva.orca.core.mastodon.BuildConfig

/** API configuration for authorization and authentication. */
internal object Mastodon {
  /** Identifies Orca amongst all Mastodon clients. */
  @Suppress("SpellCheckingInspection")
  const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

  /** Private code. */
  const val CLIENT_SECRET = BuildConfig.mastodonclientSecret

  /** Scopes required by Orca for its functionalities to work properly. */
  const val SCOPES = "read write follow"
}
