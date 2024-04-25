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

package br.com.orcinus.orca.core.mastodon.instance.registration

import android.webkit.WebView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.instance.registration.webview.loading.LoadingFinishingListenerWebViewClient
import br.com.orcinus.orca.core.mastodon.instance.registration.webview.loading.LoadingProgressChangeListenerWebChromeClient
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.platform.autos.colors.asColor
import br.com.orcinus.orca.platform.autos.kit.action.button.icon.HoverableIconButton
import br.com.orcinus.orca.platform.autos.kit.scaffold.Scaffold
import br.com.orcinus.orca.platform.autos.theme.AutosTheme
import br.com.orcinus.orca.platform.autos.theme.MultiThemePreview
import java.net.URI

/** Default values used by a [MastodonRegistration]. */
private object MastodonRegistrationDefaults {
  /** Size in [Dp]s of a toolbar action [HoverableIconButton]. */
  val ToolbarActionButtonSize = DpSize(width = 48.dp, height = 56.dp)
}

/**
 * [CustomTabsIntent]-like UI with a toolbar from which the [uri] can be requested to be shared and
 * that indicates the loading progress of the [WebView] in which registration is attempted to be
 * performed.
 *
 * @param domain [Domain] of the [Instance] in which registration is being tried.
 * @param uri [URI] to be loaded by the [WebView].
 * @param onWebpageLoad Callback for when the [WebView] finishes loading the webpage.
 * @param onSharing Callback for when the [uri] is requested to be shared.
 * @param onPop Callback for when the stack is requested to be popped.
 * @param modifier [Modifier] that is applied to the [Scaffold].
 * @see Instance.domain
 */
@Composable
internal fun MastodonRegistration(
  domain: Domain,
  uri: URI,
  onWebpageLoad: (WebView, hasLoadedSuccessfully: Boolean) -> Unit,
  onSharing: () -> Unit,
  onPop: () -> Unit,
  modifier: Modifier = Modifier
) {
  var webpageLoadingProgress by remember { mutableFloatStateOf(0f) }
  val webpageLoadingIndicatorColor = AutosTheme.colors.link.asColor

  Scaffold(
    modifier,
    topAppBar = {
      Column {
        ConstraintLayout(
          Modifier.background(AutosTheme.colors.surface.container.asColor).fillMaxWidth()
        ) {
          val (closeActionButton, domainText, shareActionButton) = createRefs()

          HoverableIconButton(
            onClick = onPop,
            Modifier.size(MastodonRegistrationDefaults.ToolbarActionButtonSize).constrainAs(
              closeActionButton
            ) {
              centerVerticallyTo(parent)
            }
          ) {
            Icon(
              Icons.Default.Close,
              contentDescription = stringResource(R.string.core_mastodon_close_tab),
              Modifier.align(Alignment.Center)
            )
          }

          Text(
            "$domain",
            Modifier.constrainAs(domainText) {
              start.linkTo(closeActionButton.end)
              centerVerticallyTo(parent)
            },
            fontSize = 18.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = TextStyle.Default
          )

          HoverableIconButton(
            onClick = onSharing,
            Modifier.size(MastodonRegistrationDefaults.ToolbarActionButtonSize).constrainAs(
              shareActionButton
            ) {
              end.linkTo(parent.end)
              centerVerticallyTo(parent)
            }
          ) {
            Icon(
              Icons.Default.Share,
              contentDescription = stringResource(R.string.core_mastodon_share),
              Modifier.align(Alignment.Center)
            )
          }
        }

        Box {
          HorizontalDivider()

          Canvas(Modifier.height(2.dp).fillMaxWidth()) {
            if (webpageLoadingProgress > 0f && webpageLoadingProgress < 1f) {
              drawLine(
                webpageLoadingIndicatorColor,
                start = Offset.Zero,
                end = Offset(x = size.width * webpageLoadingProgress, y = 0f),
                strokeWidth = size.height,
                StrokeCap.Square
              )
            }
          }
        }
      }
    }
  ) {
    expanded { padding ->
      AndroidView(::WebView, Modifier.padding(padding).fillMaxSize()) { webView ->
        webView.webViewClient = LoadingFinishingListenerWebViewClient { hasLoadedSuccessfully ->
          onWebpageLoad(webView, hasLoadedSuccessfully && webpageLoadingProgress == 1f)
        }
        webView.webChromeClient = LoadingProgressChangeListenerWebChromeClient {
          webpageLoadingProgress = it
        }
        webView.loadUrl("$uri")
      }
    }
  }
}

/** Preview of a [MastodonRegistration]. */
@Composable
@MultiThemePreview
private fun MastodonRegistrationPreview() {
  AutosTheme {
    MastodonRegistration(
      Domain.sample,
      Domain.sample.uri,
      onWebpageLoad = { _, _ -> },
      onSharing = {},
      onPop = {}
    )
  }
}
