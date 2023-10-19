package com.jeanbarrossilva.orca.setup.formatting

import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class FormattingSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    target.extensions.getByType(SpotlessExtension::class.java).kotlin {
      it.target("**\\/*.kt", "**\\/*.kts")
      it.ktfmt().googleStyle()
    }
  }
}
