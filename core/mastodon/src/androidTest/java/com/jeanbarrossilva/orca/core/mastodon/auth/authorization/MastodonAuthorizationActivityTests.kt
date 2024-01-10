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

package com.jeanbarrossilva.orca.core.mastodon.auth.authorization

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.instance.InstanceProvider
import com.jeanbarrossilva.orca.core.instance.domain.Domain
import com.jeanbarrossilva.orca.core.mastodon.R
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.test.sample
import com.jeanbarrossilva.orca.core.mastodon.auth.authorization.viewmodel.MastodonAuthorizationViewModel
import com.jeanbarrossilva.orca.core.module.CoreModule
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.content.SampleTermMuter
import com.jeanbarrossilva.orca.core.sample.instance.domain.sample
import com.jeanbarrossilva.orca.ext.intents.intendBrowsingTo
import com.jeanbarrossilva.orca.platform.autos.test.kit.input.text.onTextFieldErrors
import com.jeanbarrossilva.orca.platform.testing.asString
import com.jeanbarrossilva.orca.platform.testing.context
import com.jeanbarrossilva.orca.platform.ui.core.sample
import com.jeanbarrossilva.orca.std.injector.module.injection.injectionOf
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import org.junit.Rule
import org.junit.Test

internal class MastodonAuthorizationActivityTests {
  @get:Rule
  val injectorRule = InjectorTestRule {
    register(
      CoreModule(
        injectionOf { InstanceProvider.sample },
        injectionOf { Instance.sample.authenticationLock },
        injectionOf { SampleTermMuter() }
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
        com.jeanbarrossilva.orca.platform.autos.R.string
          .platform_ui_text_field_consecutive_error_message
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
        com.jeanbarrossilva.orca.platform.autos.R.string
          .platform_ui_text_field_consecutive_error_message
          .asString(R.string.core_http_authorization_invalid_domain.asString())
      )
  }

  @Test
  fun browsesToHelpArticle() {
    intendBrowsingTo("${MastodonAuthorizationActivity.helpUri}") {
      composeRule.onNodeWithText(R.string.core_http_authorization_help.asString()).performClick()
    }
  }

  @Test
  fun browsesToInstanceWhenSigningInWithValidDomain() {
    intendBrowsingTo("${MastodonAuthorizationViewModel.createURL(context, Domain.sample)}") {
      composeRule
        .onNodeWithText(R.string.core_http_authorization_domain.asString())
        .apply { performTextInput("${Domain.sample}") }
        .performImeAction()
    }
  }
}
