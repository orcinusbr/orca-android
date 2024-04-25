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
import java.util.Objects

/**
 * [URI] builder to which a scheme, a host and a path have been provided, from which query
 * parameters can be appended and a [URI] can be finally built.
 *
 * @param scheme Specification about the type of application addressed by the [URI].
 * @param host Subcomponent consisting of either a registered name (including but not limited to a
 *   hostname) or an IP address.
 * @param path Sequence of separated segments that may resemble or map exactly to a file system path
 *   but does not always imply a relation to one.
 * @see parameter
 * @see build
 */
class SegmentedURIBuilder
internal constructor(
  private val scheme: String,
  private val host: String,
  private val path: String
) {
  /**
   * Parameters that have been appended to the query of the [URI] to be built.
   *
   * @see parameter
   */
  private val query = hashMapOf<String, String>()

  override fun equals(other: Any?): Boolean {
    return other is SegmentedURIBuilder &&
      scheme == other.scheme &&
      host == other.host &&
      path == other.path
  }

  override fun hashCode(): Int {
    return Objects.hash(scheme, host, path)
  }

  /**
   * Appends a query parameter to the [URI] to be built.
   *
   * @param key Identifier of the [value].
   * @param value Information to be associated to the [key] in the query.
   */
  fun parameter(key: String, value: String): SegmentedURIBuilder {
    query[key] = value
    return this
  }

  /** Builds a [URI] with the specified components. */
  fun build(): URI {
    val query = query.map { (key, value) -> "$key=$value" }.joinToString(separator = "&")
    val fragment = null
    return URI(scheme, host, path, query, fragment)
  }
}
