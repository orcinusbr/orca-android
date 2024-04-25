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
 * [URI] builder to which both a scheme and a host have been provided, from which paths can be
 * appended, a query can be constructed and a [URI] can be finally built.
 *
 * @param scheme Specification about the type of application addressed by the [URI].
 * @param host Subcomponent consisting of either a registered name (including but not limited to a
 *   hostname) or an IP address.
 * @see path
 * @see query
 * @see build
 */
class HostedURIBuilder internal constructor(private val scheme: String, private val host: String) {
  /**
   * Segments that have been appended to the [URI] to be built.
   *
   * @see path
   */
  private val segments = mutableListOf<String>()

  /**
   * Sequence of separated segments that may resemble or map exactly to a file system path but does
   * not always imply a relation to one, composed by those that have been appended to [segments].
   */
  private val path
    get() = '/' + segments.joinToString(separator = "/")

  override fun equals(other: Any?): Boolean {
    return other is HostedURIBuilder &&
      scheme == other.scheme &&
      host == other.host &&
      segments == other.segments
  }

  override fun hashCode(): Int {
    return Objects.hash(scheme, host, segments)
  }

  /**
   * Appends a segment to the path of the [URI] to be built.
   *
   * @param path Segment that may resemble or map exactly to a file system path but does not always
   *   imply a relation to one.
   */
  fun path(path: String): HostedURIBuilder {
    segments += path
    return this
  }

  /** Allows for query parameters to be appended from a [SegmentedURIBuilder]. */
  fun query(): SegmentedURIBuilder {
    return SegmentedURIBuilder(scheme, host, path)
  }

  /** Builds a [URI] with the specified components. */
  fun build(): URI {
    val fragment = null
    return URI(scheme, host, path, fragment)
  }

  companion object {
    /**
     * Creates a [HostedURIBuilder] from which path segments can be appended to the given existing
     * [URI], allowing for a new one with the specified modifications to be built.
     *
     * @param uri [URI] based on which another one can be built.
     */
    fun from(uri: URI): HostedURIBuilder {
      return HostedURIBuilder(uri.scheme, uri.host)
    }
  }
}
