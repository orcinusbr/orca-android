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

package br.com.orcinus.orca.ext.uri.url

import java.net.URI

/**
 * Builder from which a [SchemedURLBuilder] can be obtained.
 *
 * @see scheme
 */
class URLBuilder internal constructor() {
  /**
   * Defines the scheme of the URL [URI] to be built.
   *
   * @param scheme Specification about the type of application addressed by the [URI].
   */
  fun scheme(scheme: String): SchemedURLBuilder {
    return SchemedURLBuilder(scheme)
  }
}
