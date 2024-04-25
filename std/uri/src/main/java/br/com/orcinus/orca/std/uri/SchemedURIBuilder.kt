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
 * [URI] builder to which a scheme has been provided, from which the host can be defined.
 *
 * @param scheme Specification about the type of application addressed by the [URI].
 * @see host
 */
@JvmInline
value class SchemedURIBuilder internal constructor(private val scheme: String) {
  /**
   * Defines the host of the [URI] to be built.
   *
   * @param host Subcomponent consisting of either a registered name (including but not limited to a
   *   hostname) or an IP address.
   */
  fun host(host: String): HostedURIBuilder {
    return HostedURIBuilder(scheme, host)
  }
}
