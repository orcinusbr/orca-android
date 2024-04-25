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

package br.com.orcinus.orca.std.uri

import java.net.URI

/**
 * Entrypoint that introduces an API for building [URI]s without having to rely on the unnamed and
 * error-prone parameters that the Java class' constructor requires.
 *
 * @see scheme
 */
object URIBuilder {
  /**
   * Defines the scheme of the [URI] to be built.
   *
   * @param scheme Specification about the type of application addressed by the [URI].
   */
  fun scheme(scheme: String): SchemedURIBuilder {
    return SchemedURIBuilder(scheme)
  }
}
