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

package com.jeanbarrossilva.orca.composite.timeline.text.annotated.span.category;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.compose.ui.text.SpanStyle;
import com.jeanbarrossilva.orca.core.feed.profile.Profile;
import com.jeanbarrossilva.orca.std.styledstring.style.type.Email;
import com.jeanbarrossilva.orca.std.styledstring.style.type.Hashtag;
import com.jeanbarrossilva.orca.std.styledstring.style.type.Link;
import com.jeanbarrossilva.orca.std.styledstring.style.type.Mention;
import java.net.URL;

/**
 * Creates categories for {@link Link}-based {@link SpanStyle}s' font feature settings.
 *
 * @see SpanStyle#getFontFeatureSettings()
 * @see Categorizer#categorizeAsHashtag()
 * @see Categorizer#categorizeAsEmail()
 * @see Categorizer#categorizeAsLink(String, URL)
 * @see Categorizer#categorizeAsMention(URL)
 */
public class Categorizer {
  /**
   * {@link String} that denotes the beginning of a category definition within font feature
   * settings.
   */
  @NonNull static final String PREFIX = "category: ";

  /**
   * Denotes the start of a {@link Link} spec.
   *
   * @see Categorizer#LINK_SPEC_END
   */
  @NonNull static final String LINK_SPEC_START = "url(";

  /**
   * Denotes the end of a {@link Link} spec.
   *
   * @see Categorizer#LINK_SPEC_START
   */
  @NonNull static final String LINK_SPEC_END = ")";

  /**
   * {@link String} by which an {@link Email} spec is tagged.
   *
   * @see Categorizer#categorizeAsEmail()
   */
  @NonNull static final String EMAIL_TAG = "email";

  /**
   * Category of an {@link Email}.
   *
   * @see Categorizer#categorizeAsEmail()
   */
  @NonNull private static final String EMAIL = String.format("%s %s", PREFIX, EMAIL_TAG);

  /**
   * {@link String} by which a {@link Hashtag} spec is tagged.
   *
   * @see Categorizer#categorizeAsHashtag()
   */
  @NonNull static final String HASHTAG_TAG = "hashtag";

  /**
   * {@link String} by which a {@link Mention} spec is tagged.
   *
   * @see Categorizer#categorizeAsMention(URL)
   */
  @NonNull static final String MENTION_TAG = "mention";

  /** {@link String} that precedes a {@link Hashtag} spec. */
  @NonNull static final String HASHTAG_SPEC_PREFIX = categorize(HASHTAG_TAG);

  /** {@link String} that precedes a {@link Mention} spec. */
  @NonNull static final String MENTION_SPEC_PREFIX = categorize(MENTION_TAG);

  /**
   * Creates categories for {@link Link}-based {@link SpanStyle}s' font feature settings.
   *
   * @see SpanStyle#getFontFeatureSettings()
   * @see Categorizer#categorizeAsHashtag()
   * @see Categorizer#categorizeAsEmail()
   * @see Categorizer#categorizeAsLink(String, URL)
   * @see Categorizer#categorizeAsMention(URL)
   */
  private Categorizer() {}

  /** Creates a category for a {@link Hashtag}-based {@link SpanStyle}. */
  @NonNull
  public static String categorizeAsHashtag() {
    return categorize(HASHTAG_TAG);
  }

  /** Creates a category for an {@link Email}-based {@link SpanStyle}. */
  @NonNull
  public static String categorizeAsEmail() {
    return EMAIL;
  }

  /**
   * Creates a category for a {@link Mention}-based {@link SpanStyle}.
   *
   * @param url {@link URL} that leads to the {@link Profile} that has been mentioned.
   */
  @NonNull
  public static String categorizeAsMention(URL url) {
    return categorizeAsLink(MENTION_TAG, url);
  }

  /**
   * Creates a default category (that is, one for a {@link SpanStyle} that has been created directly
   * from a {@link Link} instead of its subclasses, such as {@link Mention}).
   *
   * @param url {@link URL} to which the {@link Link} leads.
   */
  @NonNull
  public static String categorizeAsLink(URL url) {
    return categorizeAsLink(null, url);
  }

  /**
   * Creates a spec for the {@link URL}.
   *
   * @param url {@link URL} for which the spec is.
   */
  @NonNull
  @VisibleForTesting
  static String createSpec(URL url) {
    return LINK_SPEC_START + url + LINK_SPEC_END;
  }

  /**
   * Creates a default category (that is, one for a {@link SpanStyle} that has been created directly
   * from a {@link Link} instead of its subclasses, such as {@link Mention}).
   *
   * @param tag {@link String} that precedes the spec.
   * @param url {@link URL} to which the {@link Link} leads.
   */
  @NonNull
  private static String categorizeAsLink(@Nullable String tag, URL url) {
    @NonNull String spec = createSpec(url);
    if (tag != null) {
      spec = String.format("%s %s", tag, spec);
    }
    return categorize(spec);
  }

  /**
   * Turns the given suffix into a category.
   *
   * @param suffix Description of the text being stylized.
   */
  @NonNull
  private static String categorize(@NonNull String suffix) {
    return PREFIX + suffix;
  }
}
