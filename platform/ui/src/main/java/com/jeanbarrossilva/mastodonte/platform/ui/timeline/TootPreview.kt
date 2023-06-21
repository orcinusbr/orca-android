package com.jeanbarrossilva.mastodonte.platform.ui.timeline

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.jeanbarrossilva.loadable.placeholder.LargeTextualPlaceholder
import com.jeanbarrossilva.loadable.placeholder.MediumTextualPlaceholder
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.EmptyMutableInteractionSource
import com.jeanbarrossilva.mastodonte.platform.ui.html.HtmlAnnotatedString
import com.jeanbarrossilva.mastodonte.platform.ui.profile.SmallAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.profile.sample

private val nameTextStyle
    @Composable get() = MastodonteTheme.typography.bodyLarge
private val spacing
    @Composable get() = MastodonteTheme.spacings.large

@Composable
fun TootPreview(modifier: Modifier = Modifier) {
    TootPreview(
        avatar = { SmallAvatar() },
        name = { MediumTextualPlaceholder() },
        body = {
            Column(
                verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.extraSmall)
            ) {
                repeat(3) { LargeTextualPlaceholder() }
                MediumTextualPlaceholder()
            }
        },
        onClick = null,
        modifier
    )
}

@Composable
fun TootPreview(toot: Toot, onClick: () -> Unit, modifier: Modifier = Modifier) {
    TootPreview(
        avatar = { SmallAvatar(toot.author.name, toot.author.avatarURL) },
        name = { Text(toot.author.name, style = nameTextStyle) },
        body = { Text(HtmlAnnotatedString(toot.content)) },
        onClick,
        modifier
    )
}

@Composable
private fun TootPreview(
    avatar: @Composable () -> Unit,
    name: @Composable () -> Unit,
    body: @Composable () -> Unit,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember(onClick) {
        onClick?.let { MutableInteractionSource() } ?: EmptyMutableInteractionSource()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick ?: { },
        modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        interactionSource = interactionSource
    ) {
        Column(
            Modifier.padding(spacing),
            Arrangement.spacedBy(spacing)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                avatar()

                Column(verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.small)) {
                    name()
                    body()
                }
            }
        }
    }
}

@Composable
@Preview
private fun LoadingTootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview()
        }
    }
}

@Composable
@Preview
private fun LoadedTootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview(Toot.sample, onClick = { })
        }
    }
}
