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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.Placeholder
import com.jeanbarrossilva.mastodonte.platform.theme.extensions.placeholder
import com.jeanbarrossilva.mastodonte.platform.ui.user.SmallAvatar

@Composable
fun TootPreview(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val spacing = MastodonteTheme.spacings.large

    @OptIn(ExperimentalMaterial3Api::class)
    Card(
        onClick,
        modifier,
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column {
            Row(Modifier.padding(spacing), horizontalArrangement = Arrangement.spacedBy(spacing)) {
                SmallAvatar()

                Column(
                    verticalArrangement = Arrangement.spacedBy(MastodonteTheme.spacings.medium)
                ) {
                    Box(
                        Modifier
                            .placeholder(
                                Placeholder.Text { MastodonteTheme.typography.bodyLarge },
                                isVisible = true
                            )
                            .width(128.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement
                            .spacedBy(MastodonteTheme.spacings.small)
                    ) {
                        repeat(3) {
                            Box(
                                Modifier
                                    .placeholder(
                                        Placeholder.Text { MastodonteTheme.typography.bodyLarge },
                                        isVisible = true
                                    )
                                    .fillMaxWidth()
                            )
                        }

                        Box(
                            Modifier
                                .placeholder(
                                    Placeholder.Text { MastodonteTheme.typography.bodyLarge },
                                    isVisible = true
                                )
                                .fillMaxWidth(.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun TootPreviewPreview() {
    MastodonteTheme {
        Surface(color = MastodonteTheme.colorScheme.background) {
            TootPreview(onClick = { })
        }
    }
}
