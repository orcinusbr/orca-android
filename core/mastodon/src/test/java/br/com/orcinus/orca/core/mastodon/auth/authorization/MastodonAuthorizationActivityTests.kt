/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.auth.authorization

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.mastodon.R
import br.com.orcinus.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import br.com.orcinus.orca.core.module.CoreModule
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticationLock
import br.com.orcinus.orca.core.sample.auth.SampleAuthenticator
import br.com.orcinus.orca.core.sample.auth.SampleAuthorizer
import br.com.orcinus.orca.core.sample.auth.actor.SampleActorProvider
import br.com.orcinus.orca.core.sample.feed.profile.post.content.SampleTermMuter
import br.com.orcinus.orca.core.sample.instance.SampleInstanceProvider
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.platform.autos.test.kit.input.text.onTextFieldErrors
import br.com.orcinus.orca.platform.core.image.sample
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.platform.intents.test.intendBrowsingTo
import br.com.orcinus.orca.platform.testing.asString
import br.com.orcinus.orca.platform.testing.context
import br.com.orcinus.orca.std.image.compose.ComposableImageLoader
import br.com.orcinus.orca.std.injector.module.injection.lazyInjectionOf
import br.com.orcinus.orca.std.injector.test.InjectorTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class MastodonAuthorizationActivityTests {
  @get:Rule
  val injectorRule = InjectorTestRule {
    register(
      CoreModule(
        lazyInjectionOf { SampleInstanceProvider(ComposableImageLoader.Provider.sample) },
        lazyInjectionOf {
          SampleAuthenticationLock(SampleAuthorizer, SampleAuthenticator(), SampleActorProvider())
        },
        lazyInjectionOf { SampleTermMuter() }
      )
    )
  }

  @get:Rule val composeRule = createAndroidComposeRule<MastodonAuthorizationActivity>()

  @Test
  fun showsErrorWhenSigningInWithBlankDomain() {
    composeRule
      .onNodeWithText(R.string.core_http_authorization_domain.asString())
      .apply { performTextInput(" ") }
      .performImeAction()
    composeRule
      .onTextFieldErrors()
      .assertTextEquals(
        br.com.orcinus.orca.platform.autos.R.string.platform_ui_text_field_consecutive_error_message
          .asString(R.string.core_http_authorization_empty_domain.asString())
      )
  }

  @Test
  fun showsErrorWhenSigningInWithInvalidDomain() {
    composeRule
      .onNodeWithText(R.string.core_http_authorization_domain.asString())
      .apply { performTextInput("1️⃣🏛️🖼️") }
      .performImeAction()
    composeRule
      .onTextFieldErrors()
      .assertTextEquals(
        br.com.orcinus.orca.platform.autos.R.string.platform_ui_text_field_consecutive_error_message
          .asString(R.string.core_http_authorization_invalid_domain.asString())
      )
  }

  @Test
  fun browsesToHelpArticle() {
    intendBrowsingTo(MastodonAuthorizationActivity.helpURI) {
      composeRule.onNodeWithText(R.string.core_http_authorization_help.asString()).performClick()
    }
  }

  @Test
  fun browsesToInstanceWhenSigningInWithValidDomain() {
    intendBrowsingTo(MastodonAuthorizationViewModel.createURI(context, Domain.sample)) {
      composeRule
        .onNodeWithText(R.string.core_http_authorization_domain.asString())
        .apply { performTextInput("${Domain.sample}") }
        .performImeAction()
    }
  }
}
