package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

import java.util.concurrent.atomic.AtomicReference

/**
 * Groups [Int]s that are consecutive into [IntRange]s, sorting them by their first value
 * ascendingly.
 */
internal fun Iterable<Int>.consecutively(): List<IntRange> {
  val count = count()
  if (count == 0) {
    return emptyList()
  }
  val (start, end) = AtomicReference<Int>() to AtomicReference<Int>()
  val grouped = arrayOfNulls<IntRange>(size = count)
  var groupIndex = 0
  val group = { grouped[groupIndex++] = start.get()!!..end.get()!! }
  val isLastIntIndex = { intIndex: Int -> intIndex == count - 1 }
  forEachIndexed { intIndex, int ->
    if (start.get() == null) {
      start.set(int)
    } else if (end.get() == null || int == end.get()!!.inc()) {
      end.set(int)
      if (isLastIntIndex(intIndex)) {
        group()
      }
    } else {
      group()
      start.set(int)
      if (isLastIntIndex(intIndex)) {
        end.set(int)
        group()
      } else {
        end.set(null)
      }
    }
  }
  @Suppress("UNCHECKED_CAST")
  return (grouped.take(groupIndex) as List<IntRange>).sortedBy(IntRange::first)
}
