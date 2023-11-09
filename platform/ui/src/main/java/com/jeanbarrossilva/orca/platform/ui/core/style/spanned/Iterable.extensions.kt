package com.jeanbarrossilva.orca.platform.ui.core.style.spanned

/**
 * Groups [Int]s that are consecutive into [IntRange]s, sorting them by their first value
 * ascendingly.
 */
internal fun Iterable<Int>.consecutively(): List<IntRange> {
  val count = count()
  if (count == 0) {
    return emptyList()
  }
  var (start, end) = null as Int? to null as Int?
  val grouped = arrayOfNulls<IntRange>(size = count)
  var groupIndex = 0
  val group = { grouped[groupIndex++] = start!!..end!! }
  val isLastIntIndex = { intIndex: Int -> intIndex == count - 1 }
  forEachIndexed { intIndex, int ->
    if (start == null) {
      start = int
    } else if (end == null || int == end!!.inc()) {
      end = int
      if (isLastIntIndex(intIndex)) {
        group()
      }
    } else {
      group()
      start = int
      if (isLastIntIndex(intIndex)) {
        end = int
        group()
      } else {
        end = null
      }
    }
  }
  @Suppress("UNCHECKED_CAST")
  return (grouped.take(groupIndex) as List<IntRange>).sortedBy(IntRange::first)
}
