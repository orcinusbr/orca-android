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
        while read local_ref local_oid remote_ref remote_oid; do
          has_changes=${'$'}([ "${'$'}local_oid" != "${'$'}zero" ]; echo ${'$'}?)
          if [ ${'$'}has_changes -eq 0 ]; then
            ./gradlew build
            ./gradlew connectedAndroidTest
          fi
        done
        exit 0
      """
          .trimIndent()
      )
    ProcessBuilder().command("chmod", "+x", "$prePushHookPath")
  }
}
