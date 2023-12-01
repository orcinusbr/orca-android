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
