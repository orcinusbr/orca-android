/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.platform.ui.component.timeline.post.figure.link

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Headline
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.highlight.createSample
import com.jeanbarrossilva.orca.platform.autos.border
import com.jeanbarrossilva.orca.platform.autos.colors.asColor
import com.jeanbarrossilva.orca.platform.autos.forms.asShape
import com.jeanbarrossilva.orca.platform.autos.kit.action.Hoverable
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.autos.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.core.image.sample
import com.jeanbarrossilva.orca.std.image.compose.ComposableImageLoader
import com.jeanbarrossilva.orca.std.image.compose.SomeComposableImageLoader

/** Tag that identifies a [LinkCard] for testing purposes. */
const val LINK_CARD_TAG = "link-card"

@Composable
fun LinkCard(headline: Headline, onClick: () -> Unit, modifier: Modifier = Modifier) {
  val shape = AutosTheme.forms.large.asShape

  Hoverable(
    modifier
      .border(shape)
      .clip(shape)
      .clickable(onClick = onClick)
      .background(AutosTheme.colors.surface.container.asColor)
      .testTag(LINK_CARD_TAG)
  ) {
    Column {
      (headline.coverLoader as SomeComposableImageLoader?)
        ?.load()
        ?.invoke(
          stringResource(R.string.platform_ui_link_card_cover, headline.title),
          RectangleShape,
          ComposableImageLoader.DefaultContentScale,
          Modifier.aspectRatio(16f / 9f).fillMaxWidth()
        )

      Column(
        Modifier.padding(AutosTheme.spacings.medium.dp),
        Arrangement.spacedBy(AutosTheme.spacings.small.dp)
      ) {
        ProvideTextStyle(AutosTheme.typography.bodyLarge) { Text(headline.title) }

        headline.subtitle?.let {
          ProvideTextStyle(AutosTheme.typography.bodySmall) {
            Text(it, overflow = TextOverflow.Ellipsis, maxLines = 4)
          }
        }
      }
    }
  }
}

@Composable
internal fun LinkCard(modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
  LinkCard(Headline.createSample(ComposableImageLoader.Provider.sample), onClick, modifier)
}

@Composable
@MultiThemePreview
private fun LinkCardPreview() {
  AutosTheme { LinkCard() }
}
