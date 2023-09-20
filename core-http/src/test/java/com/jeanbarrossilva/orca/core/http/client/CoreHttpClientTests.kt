package com.jeanbarrossilva.orca.core.http.client

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.jeanbarrossilva.orca.core.http.client.test.assertThatRequestAuthorizationHeaderOf
import com.jeanbarrossilva.orca.core.http.client.test.runAuthenticatedTest
import com.jeanbarrossilva.orca.core.http.client.test.runUnauthenticatedTest
import io.ktor.http.parametersOf
import kotlin.test.Test

internal class CoreHttpClientTests {
    @Test
    fun `GIVEN an authenticate-and-get request with an unauthenticated actor WHEN performing it THEN the actor is requested to be authenticated`() { // ktlint-disable max-line-length
        var isAuthenticated = false
        runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
            client.authenticateAndGet(authenticationLock, route = "")
            assertThat(isAuthenticated).isTrue()
        }
    }

    @Test
    fun `GIVEN an authenticate-and-get request with an authenticated actor WHEN performing it THEN the bearer auth header has been set`() { // ktlint-disable max-line-length
        runAuthenticatedTest {
            assertThatRequestAuthorizationHeaderOf(
                client.authenticateAndGet(authenticationLock, route = "")
            )
                .isEqualTo("Bearer ${actor.accessToken}")
        }
    }

    @Test
    fun `GIVEN an authenticate-and-post request with an unauthenticated actor WHEN performing it THEN the actor is requested to be authenticated`() { // ktlint-disable max-line-length
        var isAuthenticated = false
        runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
            client.authenticateAndPost(authenticationLock, route = "")
            assertThat(isAuthenticated).isTrue()
        }
    }

    @Test
    fun `GIVEN an authenticate-and-post request with an authenticated actor WHEN performing it THEN the bearer auth header has been set`() { // ktlint-disable max-line-length
        runAuthenticatedTest {
            assertThatRequestAuthorizationHeaderOf(
                client.authenticateAndPost(authenticationLock, route = "")
            )
                .isEqualTo("Bearer ${actor.accessToken}")
        }
    }

    @Test
    fun `GIVEN an authenticate-and-submit-form request with an unauthenticated actor WHEN performing it THEN the actor is requested to be authenticated`() { // ktlint-disable max-line-length
        var isAuthenticated = false
        runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
            client.authenticateAndSubmitForm(authenticationLock, route = "", parametersOf())
            assertThat(isAuthenticated).isTrue()
        }
    }

    @Test
    fun `GIVEN an authenticate-and-submit-form request with an authenticated actor WHEN performing it THEN the bearer auth header has been set`() { // ktlint-disable max-line-length
        runAuthenticatedTest {
            assertThatRequestAuthorizationHeaderOf(
                client.authenticateAndSubmitForm(authenticationLock, route = "", parametersOf())
            )
                .isEqualTo("Bearer ${actor.accessToken}")
        }
    }

    @Test
    fun `GIVEN an authenticate-and-submit-form-with-binary-data request with an unauthenticated actor WHEN performing it THEN the actor is requested to be authenticated`() { // ktlint-disable max-line-length
        var isAuthenticated = false
        runUnauthenticatedTest(onAuthentication = { isAuthenticated = true }) {
            client.authenticateAndSubmitFormWithBinaryData(
                authenticationLock,
                route = "",
                formData = emptyList()
            )
            assertThat(isAuthenticated).isTrue()
        }
    }

    @Test
    fun `GIVEN an authenticate-and-submit-form-with-binary-data request with an authenticated actor WHEN performing it THEN the bearer auth header has been set`() { // ktlint-disable max-line-length
        runAuthenticatedTest {
            assertThatRequestAuthorizationHeaderOf(
                client.authenticateAndSubmitFormWithBinaryData(
                    authenticationLock,
                    route = "",
                    formData = emptyList()
                )
            )
                .isEqualTo("Bearer ${actor.accessToken}")
        }
    }
}
