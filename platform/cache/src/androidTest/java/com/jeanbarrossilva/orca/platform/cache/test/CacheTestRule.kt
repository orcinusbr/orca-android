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

package com.jeanbarrossilva.orca.platform.cache.test

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.orca.platform.cache.database.CacheDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import org.junit.rules.ExternalResource

internal class CacheTestRule(private val coroutineScope: TestScope) : ExternalResource() {
  private lateinit var database: CacheDatabase

  val cache = TestCache(coroutineScope.testScheduler, ::database)

  override fun before() {
    val context = InstrumentationRegistry.getInstrumentation().context
    database = Room.databaseBuilder(context, CacheDatabase::class.java, TestCache.NAME).build()
  }

  override fun after() {
    coroutineScope.launch { cache.terminate() }
  }
}
