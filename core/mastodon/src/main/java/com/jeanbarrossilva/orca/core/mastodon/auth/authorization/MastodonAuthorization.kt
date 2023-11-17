package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.jeanbarrossilva.loadable.Loadable
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.mastodon.R
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable.list.SelectableList
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.selectable.list.selectFirst
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import com.jeanbarrossilva.orca.core.sample.instance.domain.samples
import com.jeanbarrossilva.orca.platform.theme.MultiThemePreview
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.extensions.plus
import com.jeanbarrossilva.orca.platform.theme.kit.action.button.PrimaryButton
import com.jeanbarrossilva.orca.platform.theme.kit.input.option.list.Options
import com.jeanbarrossilva.orca.platform.theme.kit.input.text.TextField
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.Scaffold
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.button.ButtonBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.TopAppBar
import com.jeanbarrossilva.orca.platform.theme.kit.scaffold.bar.top.text.AutoSizeText

/**
 * Screen that presents username and instance fields for the user to fill in order for them to be
 * authenticated.
 *
 * @param viewModel [MastodonAuthorizationViewModel] by which the [Domain]s will be loaded and to
 *   which updates to the search query and the selection will be sent.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun MastodonAuthorization(
  viewModel: MastodonAuthorizationViewModel,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQueryFlow.collectAsState()
  val instanceDomainSelectablesLoadable by
    viewModel.instanceDomainSelectablesLoadableFlow.collectAsState()

  when (
    @Suppress("NAME_SHADOWING")
    val instanceDomainSelectablesLoadable = instanceDomainSelectablesLoadable
  ) {
    is Loadable.Loading -> MastodonAuthorization(modifier)
    is Loadable.Loaded ->
      MastodonAuthorization(
        searchQuery,
        onSearch = viewModel::search,
        instanceDomainSelectablesLoadable.content,
        onSelection = viewModel::select,
        onSignIn = viewModel::authorize
      )
    is Loadable.Failed -> Unit
  }
}

/**
 * Screen that presents loading [Domain]s.
 *
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun MastodonAuthorization(modifier: Modifier = Modifier) {
  MastodonAuthorization(
    searchField = {},
    instanceDomains = { Options() },
    onSignIn = {},
    isSignInButtonEnabled = false,
    modifier
  )
}

/**
 * Screen that presents the [Domain]s that can be selected by the user for them to authenticate.
 *
 * @param searchQuery Query with which the [Domain]s will be filtered.
 * @param onSearch Callback run whenever the user inputs to the search [TextField].
 * @param instanceDomains [Domain]s to be shown to the user.
 * @param onSelection Callback run whenever a [Domain] is selected.
 * @param onSignIn Callback run whenever the sign-in [PrimaryButton] is clicked.
 * @param modifier [Modifier] to be applied to the underlying [Box].
 */
@Composable
internal fun MastodonAuthorization(
  searchQuery: String,
  onSearch: (query: String) -> Unit,
  instanceDomains: SelectableList<Domain>,
  onSelection: (Domain) -> Unit,
  onSignIn: () -> Unit,
  modifier: Modifier = Modifier
) {
  MastodonAuthorization(
    searchField = {
      TextField(
        searchQuery,
        onSearch,
        Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        isSingleLined = true
      ) {
        Text(stringResource(R.string.core_http_authorization_search))
      }
    },
    instanceDomains = {
      Options(onSelection = { onSelection(instanceDomains[it].value) }) {
        instanceDomains.forEach { option { Text("${it.value}") } }
      }
    },
    onSignIn,
    isSignInButtonEnabled = true,
    modifier
  )
}

@Composable
internal fun MastodonAuthorization(
  searchField: @Composable LazyItemScope.() -> Unit,
  instanceDomains: @Composable LazyItemScope.() -> Unit,
  onSignIn: () -> Unit,
  isSignInButtonEnabled: Boolean,
  modifier: Modifier = Modifier
) {
  val lazyListState = rememberLazyListState()

  @OptIn(ExperimentalMaterial3Api::class)
  val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

  val isHeaderHidden by
    remember(lazyListState) { derivedStateOf { lazyListState.firstVisibleItemIndex > 0 } }
  val headerTitle = stringResource(R.string.core_http_authorization_account_origin)
  val spacing = OrcaTheme.spacings.extraLarge

  Box(modifier) {
    Scaffold(
      buttonBar = {
        ButtonBar(lazyListState) {
          PrimaryButton(onClick = onSignIn, isEnabled = isSignInButtonEnabled) {
            Text(stringResource(R.string.core_http_authorization_sign_in))
          }
        }
      }
    ) {
      LazyColumn(
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = it + PaddingValues(spacing)
      ) {
        item {
          Column(
            verticalArrangement = Arrangement.spacedBy(OrcaTheme.spacings.small),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              stringResource(R.string.core_http_authorization_welcome),
              textAlign = TextAlign.Center,
              style = OrcaTheme.typography.headlineLarge
            )

            Text(headerTitle, textAlign = TextAlign.Center, style = OrcaTheme.typography.titleSmall)
          }
        }

        item(content = searchField)
        item(content = instanceDomains)
      }
    }

    AnimatedVisibility(
      visible = isHeaderHidden,
      enter = slideInVertically { -it },
      exit = slideOutVertically { -it }
    ) {
      Column {
        @OptIn(ExperimentalMaterial3Api::class)
        TopAppBar(title = { AutoSizeText(headerTitle) }, scrollBehavior = topAppBarScrollBehavior)

        Divider()
      }
    }
  }
}

/** Preview of a loading [MastodonAuthorization] screen. */
@Composable
@MultiThemePreview
private fun LoadingMastodonAuthorizationPreview() {
  OrcaTheme { MastodonAuthorization() }
}

/** Preview of a loaded [MastodonAuthorization] screen. */
@Composable
@MultiThemePreview
private fun LoadedMastodonAuthorizationPreview() {
  OrcaTheme {
    MastodonAuthorization(
      searchQuery = "",
      onSearch = {},
      instanceDomains = Domain.samples.selectFirst(),
      onSelection = {},
      onSignIn = {}
    )
  }
}
