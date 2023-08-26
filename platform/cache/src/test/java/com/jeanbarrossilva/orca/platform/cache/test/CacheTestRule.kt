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
        database =
            Room.databaseBuilder(context, CacheDatabase::class.java, CacheDatabase.NAME).build()
    }

    override fun after() {
        coroutineScope.launch {
            cache.terminate()
        }
    }
}
