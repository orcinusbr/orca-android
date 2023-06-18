package com.jeanbarrossilva.mastodonte.platform.ui.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.loadable.ifLoaded
import com.jeanbarrossilva.loadable.map
import com.jeanbarrossilva.mastodonte.core.profile.toot.Toot
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import com.jeanbarrossilva.mastodonte.platform.ui.profile.Avatar
import com.jeanbarrossilva.mastodonte.platform.ui.profile.SmallAvatar
import com.jeanbarrossilva.mastodonte.platform.ui.profile.html.HtmlAnnotatedString
import com.jeanbarrossilva.mastodonte.platform.ui.profile.sample

@Composable
fun TootPreview(loadable: Loadable<Toot>, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val avatarLoadable = remember(loadable) {
        loadable.map {
            Avatar(it.author.name, it.author.avatarURL)
        }
    }
    val bodyLoadable = remember(loadable) { loadable.map(Toot::content) }
    val nameStyle = MastodonteTheme.typography.bodyLarge
    val spacing = MastodonteTheme.spacings.large
    val isLoading = loadable is Loadable.Loading

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick,
        modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            Row(Modifier.padding(spacing), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                SmallAvatar(avatarLoadable)

                Column(
                    verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.small)
                ) {
                    Text(
                        loadable.ifLoaded { author.name }.orEmpty(),
                        Modifier
                            .placeholder(Placeholder.Text { nameStyle }, isVisible = isLoading)
                            .width(128.dp),
                        style = nameStyle
                    )

                    Body(bodyLoadable)
                }
            }
        }
    }
}

@Composable
private fun Body(loadable: Loadable<String>, modifier: Modifier = Modifier) {
    when (loadable) {
        is Loadable.Loading -> LoadingBody(modifier)
        is Loadable.Loaded -> Text(HtmlAnnotatedString(loadable.content), modifier)
        is Loadable.Failed -> { }
    }
}

@Composable
private fun LoadingBody(modifier: Modifier = Modifier) {
    Column(modifier, Arrangement.spacedBy(MastodonteTheme.spacings.small)) {
        repeat(3) {
            Box(
                Modifier
                    .placeholder(Placeholder.Text(), isVisible = true)
                    .fillMaxWidth()
            )
        }

        Box(
            Modifier
                .placeholder(Placeholder.Text(), isVisible = true)
                .fillMaxWidth(.5f)
        )
    }
}

@Composable
@Preview
private fun TootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview(Loadable.Loaded(Toot.sample), onClick = { })
        }
    }
}
