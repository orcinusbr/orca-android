package com.jeanbarrossilva.orca.std.styledstring

import java.io.Serializable

/** Indicates where and how a target has been stylized. */
interface Style : Serializable {
  /** Indices at which both the style symbols and the target are in the whole [String]. */
  val indices: IntRange

  /** [Style] that requires its targets to match the [regex]. */
  abstract class Constrained : Style {
    /** [Regex] that targets to which this [Style] is applied should match. */
    internal abstract val regex: Regex

    /**
     * [IllegalArgumentException] thrown if the target is an invalid one for this [Style].
     *
     * @param target Target that's invalid.
     */
    inner class InvalidTargetException internal constructor(target: String) :
      IllegalArgumentException("Target doesn't match regex ($regex): \"$target\".")
  }

  /**
   * Copies this [Style].
   *
   * @param indices Indices at which both the style symbols and the target are in the whole
   *   [String].
   */
  fun at(indices: IntRange): Style
}
