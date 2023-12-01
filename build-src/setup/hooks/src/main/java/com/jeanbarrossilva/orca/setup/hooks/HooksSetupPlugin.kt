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

package com.jeanbarrossilva.orca.setup.hooks

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.writeText
import org.gradle.api.Plugin
import org.gradle.api.Project

class HooksSetupPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    val rootDirPath = Path.of(target.rootDir.absolutePath)
    val prePushHookPath = rootDirPath.resolve(".git").resolve("hooks").resolve("pre-push")
    Files.deleteIfExists(prePushHookPath)
    Files.createFile(prePushHookPath)
      .writeText(
        """
          #!/bin/bash

          remote="${'$'}1"
          url="${'$'}2"
          zero=${'$'}(git hash-object --stdin </dev/null | tr '[0-9a-f]' '0')

          try_to_gradlew() {
            local has_failed=0
             while IFS= read -r line; do
              echo "${'$'}line"
              if [[ "${'$'}line" == "BUILD FAILED"* ]]; then
                has_failed=1
              fi
            done < <(./gradlew "${'$'}1" 2>&1)
            if [ ${'$'}has_failed -eq 1 ]; then
              echo "Build failed, aborting push."
              exit 1
            fi
          }

          while read local_ref local_oid remote_ref remote_oid; do
            has_changes=${'$'}([ "${'$'}local_oid" != "${'$'}zero" ]; echo ${'$'}?)
            if [ ${'$'}has_changes -eq 0 ]; then
              try_to_gradlew spotlessApply
              try_to_gradlew build
              try_to_gradlew connectedAndroidTest
            fi
          done
          exit 0
      """
          .trimIndent()
      )
    ProcessBuilder()
      .command("git", "config", "core.hooksPath", ".git/hooks")
      .command("chmod", "+x", "$prePushHookPath")
      .start()
  }
}
