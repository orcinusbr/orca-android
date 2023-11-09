package com.jeanbarrossilva.orca.app

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.app.test.launchOrcaActivity
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizationActivity
import org.junit.Rule
import org.junit.Test

internal class OrcaActivityTests {
  @get:Rule val intentsRule = IntentsRule()

  @Test
  fun navigatesToAuthorization() {
    launchOrcaActivity().use {
      intended(hasComponent(HttpAuthorizationActivity::class.qualifiedName))
    }
  }
}