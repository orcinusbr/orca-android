package com.jeanbarrossilva.orca.std.buildable.processor.parsing;

import java.lang.reflect.Array;
import java.util.Arrays;
import kotlin.ranges.IntRange;
import org.jetbrains.annotations.NotNull;

/**
 * Extracts the indices of opening and closing delimiters of multiline comments within various lines
 * through {@link MultilineCommentsIndices#of}.
 */
public class MultilineCommentsIndices {
  /** Denotes that a slot of an {@link Array} of indices is empty. */
  private static final int EMPTY_INDEX_SLOT = -1;

  /** {@link IllegalStateException} thrown if a multiline comment body doesn't have a delimiter. */
  public static class NonDelimitedBodyException extends IllegalStateException {
    private NonDelimitedBodyException() {
      super("Multiline comment body doesn't start with a delimiter.");
    }
  }

  /**
   * Extracts the indices of opening and closing delimiters of multiline comments within the given
   * lines.
   */
  @NotNull
  public static IntRange[] of(@NotNull String[] lines) {
    int lineCount = lines.length;
    removeIndentation(lines, lineCount);
    int[] openingIndices = findOpeningIndices(lines, lineCount);
    int[] closingIndices = findClosingIndices(openingIndices, lines, lineCount);
    IntRange[] ranges = new IntRange[openingIndices.length / 2];
    int currentRangeIndex = 0;
    for (int openingIndex : openingIndices) {
      if (openingIndex != EMPTY_INDEX_SLOT) {
        ranges[currentRangeIndex] = new IntRange(openingIndex, closingIndices[currentRangeIndex]);
      }
      currentRangeIndex++;
    }
    return ranges;
  }

  /** Removes any indentation the given lines might have. */
  private static void removeIndentation(String[] lines, int lineCount) {
    for (int index = 0; index < lineCount; index++) {
      lines[index] = lines[index].stripLeading();
    }
  }

  /**
   * Finds the indices of opening multiline comment delimiters.
   *
   * @return An {@link Array} that can hold as many {@link Integer}s as 50% of the given line count.
   *     If the amount of indices is less than that capacity, then each remaining slot will be set
   *     to {@link MultilineCommentsIndices#EMPTY_INDEX_SLOT}.
   */
  @NotNull
  private static int[] findOpeningIndices(@NotNull String[] lines, int lineCount) {
    int capacity = (int) Math.ceil(lineCount / 2.0);
    int[] openingIndices = new int[capacity];
    Arrays.fill(openingIndices, EMPTY_INDEX_SLOT);
    int currentOpeningIndexIndex = 0;
    for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
      if (isOpening(lines[lineIndex])) {
        openingIndices[currentOpeningIndexIndex++] = lineIndex;
      }
    }
    return openingIndices;
  }

  /** Finds the indices of closing multiline comment delimiters. */
  @NotNull
  private static int[] findClosingIndices(
      @NotNull int[] openingIndices, @NotNull String[] lines, int lineCount) {
    int capacity = 0;
    for (int openingIndex : openingIndices) {
      if (openingIndex != EMPTY_INDEX_SLOT) {
        capacity++;
      } else {
        break;
      }
    }
    int[] closingIndices = new int[capacity];
    int lastClosingIndexIndex = -1;
    Arrays.fill(closingIndices, EMPTY_INDEX_SLOT);
    if (capacity > 0) {
      for (int lineIndex = openingIndices[capacity - 1] + 1; lineIndex < lineCount; lineIndex++) {
        if (!isMultilineCommentBody(lines[lineIndex])) {
          try {
            closingIndices[++lastClosingIndexIndex] = lineIndex;
          } catch (ArrayIndexOutOfBoundsException exception) {
            throw new NonDelimitedBodyException();
          }
        }
      }
    }
    return closingIndices;
  }

  /** Whether the given line is the opening of a multiline comment. */
  private static boolean isOpening(String line) {
    return line.stripLeading().startsWith("/*");
  }

  /** Whether the given line is the body of a multiline comment. */
  private static boolean isMultilineCommentBody(String line) {
    return line.startsWith("*") && !line.startsWith("*/");
  }
}
