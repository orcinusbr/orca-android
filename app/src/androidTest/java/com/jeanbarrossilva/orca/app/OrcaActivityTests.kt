package com.jeanbarrossilva.orca.app

import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsRule
import com.jeanbarrossilva.orca.app.test.TestOrcaActivity
import com.jeanbarrossilva.orca.core.http.auth.authorization.HttpAuthorizationActivity
import org.junit.Rule
import org.junit.Test

internal class OrcaActivityTests {
  @get:Rule val intentsRule = IntentsRule()

  @Test
  fun navigatesToAuthorization() {
    launchActivity<TestOrcaActivity>().use {
      intended(hasComponent(HttpAuthorizationActivity::class.qualifiedName))
    }
  }
}
