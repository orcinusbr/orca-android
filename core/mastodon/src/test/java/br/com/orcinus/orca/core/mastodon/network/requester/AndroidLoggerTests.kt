/*
 * Copyright © 2023–2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.network.requester

import android.util.Log
import br.com.orcinus.orca.core.mastodon.network.client.ClientResponseProvider
import br.com.orcinus.orca.core.mastodon.network.client.createHttpClientEngineFactory
import br.com.orcinus.orca.core.mastodon.network.requester.Logger.Android.format
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.respondError
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coVerify
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class AndroidLoggerTests {
  @Test
  fun infoCallsAndroidLogI() {
    lateinit var response: HttpResponse
    mockkStatic(Log::class) {
      runTest {
        response =
          HttpClient(createHttpClientEngineFactory(ClientResponseProvider { respondOk("😮") })) {
              Logger.Android.start(this)
            }
            .get("/api/v1/resource")
      }
      coVerify { Log.i(Logger.Android.TAG, response.format()) }
    }
  }

  @Test
  fun errorCallsAndroidLogE() {
    lateinit var response: HttpResponse
    mockkStatic(Log::class) {
      runTest {
        response =
          HttpClient(
              createHttpClientEngineFactory(
                ClientResponseProvider { respondError(HttpStatusCode.NotImplemented, "😵") }
              )
            ) {
              Logger.Android.start(this)
            }
            .get("/api/v1/resource")
      }
      coVerify { Log.e(Logger.Android.TAG, response.format()) }
    }
  }
}
