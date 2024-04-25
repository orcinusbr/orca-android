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

package br.com.orcinus.orca.composite.timeline.text.annotated.span.category;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.compose.ui.text.SpanStyle;
import br.com.orcinus.orca.core.feed.profile.Profile;
import br.com.orcinus.orca.std.markdown.style.Style;
import java.net.URI;

/**
 * Creates categories for {@link Style.Link}-based {@link SpanStyle}s' font feature settings.
 *
 * @see SpanStyle#getFontFeatureSettings()
 * @see Categorizer#categorizeAsHashtag()
 * @see Categorizer#categorizeAsEmail()
 * @see Categorizer#categorizeAsLink(String, URI)
 * @see Categorizer#categorizeAsMention(URI)
 */
public class Categorizer {
  /**
   * {@link String} that denotes the beginning of a category definition within font feature
   * settings.
   */
  @NonNull static final String PREFIX = "category: ";

  /**
   * Denotes the start of a {@link Style.Link} spec.
   *
   * @see Categorizer#LINK_SPEC_END
   */
  @NonNull static final String LINK_SPEC_START = "url(";

  /**
   * Denotes the end of a {@link Style.Link} spec.
   *
   * @see Categorizer#LINK_SPEC_START
   */
  @NonNull static final String LINK_SPEC_END = ")";

  /**
   * {@link String} by which an {@link Style.Email} spec is tagged.
   *
   * @see Categorizer#categorizeAsEmail()
   */
  @NonNull static final String EMAIL_TAG = "email";

  /**
   * Category of an {@link Style.Email}.
   *
   * @see Categorizer#categorizeAsEmail()
   */
  @NonNull private static final String EMAIL = String.format("%s %s", PREFIX, EMAIL_TAG);

  /**
   * {@link String} by which a {@link Style.Hashtag} spec is tagged.
   *
   * @see Categorizer#categorizeAsHashtag()
   */
  @NonNull static final String HASHTAG_TAG = "hashtag";

  /**
   * {@link String} by which a {@link Style.Mention} spec is tagged.
   *
   * @see Categorizer#categorizeAsMention(URI)
   */
  @NonNull static final String MENTION_TAG = "mention";

  /** {@link String} that precedes a {@link Style.Hashtag} spec. */
  @NonNull static final String HASHTAG_SPEC_PREFIX = categorize(HASHTAG_TAG);

  /** {@link String} that precedes a {@link Style.Mention} spec. */
  @NonNull static final String MENTION_SPEC_PREFIX = categorize(MENTION_TAG);

  /**
   * Creates categories for {@link Style.Link}-based {@link SpanStyle}s' font feature settings.
   *
   * @see SpanStyle#getFontFeatureSettings()
   * @see Categorizer#categorizeAsHashtag()
   * @see Categorizer#categorizeAsEmail()
   * @see Categorizer#categorizeAsLink(String, URI)
   * @see Categorizer#categorizeAsMention(URI)
   */
  private Categorizer() {}

  /** Creates a category for a {@link Style.Hashtag}-based {@link SpanStyle}. */
  @NonNull
  public static String categorizeAsHashtag() {
    return categorize(HASHTAG_TAG);
  }

  /** Creates a category for an {@link Style.Email}-based {@link SpanStyle}. */
  @NonNull
  public static String categorizeAsEmail() {
    return EMAIL;
  }

  /**
   * Creates a category for a {@link Style.Mention}-based {@link SpanStyle}.
   *
   * @param uri {@link URI} that leads to the {@link Profile} that has been mentioned.
   */
  @NonNull
  public static String categorizeAsMention(URI uri) {
    return categorizeAsLink(MENTION_TAG, uri);
  }

  /**
   * Creates a default category (that is, one for a {@link SpanStyle} that has been created directly
   * from a {@link Style.Link} instead of its subclasses, such as {@link Style.Mention}).
   *
   * @param uri {@link URI} to which the {@link Style.Link} leads.
   */
  @NonNull
  public static String categorizeAsLink(URI uri) {
    return categorizeAsLink(null, uri);
  }

  /**
   * Creates a spec for the {@link URI}.
   *
   * @param uri {@link URI} for which the spec is.
   */
  @NonNull
  @VisibleForTesting
  static String createSpec(URI uri) {
    return LINK_SPEC_START + uri + LINK_SPEC_END;
  }

  /**
   * Creates a default category (that is, one for a {@link SpanStyle} that has been created directly
   * from a {@link Style.Link} instead of its subclasses, such as {@link Style.Mention}).
   *
   * @param tag {@link String} that precedes the spec.
   * @param uri {@link URI} to which the {@link Style.Link} leads.
   */
  @NonNull
  private static String categorizeAsLink(@Nullable String tag, URI uri) {
    @NonNull String spec = createSpec(uri);
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
