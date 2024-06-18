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
import java.util.Objects

/**
 * URL [URI] builder to which both a scheme and a host have been provided, from which paths can be
 * appended, a query can be constructed and a [URI] can be finally built.
 *
 * @param scheme Specification about the type of application addressed by the [URI].
 * @param host Subcomponent consisting of either a registered name (including but not limited to a
 *   hostname) or an IP address.
 * @see path
 * @see query
 * @see build
 */
class HostedURLBuilder internal constructor(private val scheme: String, private val host: String) {
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
    return other is HostedURLBuilder &&
      scheme == other.scheme &&
      host == other.host &&
      segments == other.segments
  }

  override fun hashCode(): Int {
    return Objects.hash(scheme, host, segments)
  }

  /**
   * Appends a segment to the path of the URL [URI] to be built.
   *
   * @param path Segment that may resemble or map exactly to a file system path but does not always
   *   imply a relation to one.
   */
  fun path(path: String): HostedURLBuilder {
    segments += path
    return this
  }

  /** Allows for query parameters to be appended from a [SegmentedURLBuilder]. */
  fun query(): SegmentedURLBuilder {
    return SegmentedURLBuilder(scheme, host, path)
  }

  /** Builds a URL [URI] with the specified components. */
  fun build(): URI {
    val fragment = null
    return URI(scheme, host, path, fragment)
  }

  companion object {
    /**
     * Creates a [HostedURLBuilder] from which path segments can be appended to the given existing
     * URL [URI], allowing for a new one with the specified modifications to be built.
     *
     * @param uri [URI] based on which another one can be built.
     */
    fun from(uri: URI): HostedURLBuilder {
      return HostedURLBuilder(uri.scheme, uri.host)
    }
  }
}
