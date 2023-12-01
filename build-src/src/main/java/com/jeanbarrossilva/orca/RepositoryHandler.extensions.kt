/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanbarrossilva.orca

import java.net.URI
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository

/**
 * Adds the repository in which [αὐτός](https://github.com/the-orca-app/autos) is located.
 *
 * @param project [Project] to which the repository is being added.
 */
fun RepositoryHandler.autos(project: Project): MavenArtifactRepository {
  return gitHubPackages(project, route = "the-orca-app/autos")
}

/** Adds [chRyNaN](https://github.com/chRyNaN)'s repository. * */
fun RepositoryHandler.chrynan(): MavenArtifactRepository {
  return maven { url = URI.create("https://repo.repsy.io/mvn/chrynan/public") }
}

/**
 * Adds the repository in which [Loadable](https://github.com/jeanbarrossilva/Aurelius) is located.
 *
 * @param project [Project] to which the repository is being added.
 */
fun RepositoryHandler.loadable(project: Project): MavenArtifactRepository {
  return gitHubPackages(project, route = "jeanbarrossilva/loadable-android")
}

/**
 * Adds a GitHub Packages repository.
 *
 * @param project [Project] to which the repository is being added.
 * @param route Path that leads to the repository.
 */
private fun RepositoryHandler.gitHubPackages(
  project: Project,
  route: String
): MavenArtifactRepository {
  return maven {
    url = URI.create("https://maven.pkg.github.com/$route")

    credentials {
      with(project.projectDir.properties("local")) {
        username = getProperty("github.username") ?: System.getenv("GITHUB_USERNAME")
        password = getProperty("github.token") ?: System.getenv("GITHUB_TOKEN")
      }
    }
  }
}
