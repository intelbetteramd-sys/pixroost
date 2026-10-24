package app.pixroost.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AssetDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.assetDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun feedIsNewestFirstAndUndatedLast() = runTest {
        val old = dao.insert(asset(takenAt = 100))
        val undated = dao.insert(asset(takenAt = null))
        val new = dao.insert(asset(takenAt = 300))

        val feed = dao.getFeedPage(limit = 10, offset = 0)

        assertEquals(listOf(new, old, undated), feed.map { it.id })
    }

    @Test
    fun feedPagesDoNotOverlap() = runTest {
        repeat(times = 5) { dao.insert(asset(takenAt = it.toLong())) }

        val first = dao.getFeedPage(limit = 2, offset = 0)
        val second = dao.getFeedPage(limit = 2, offset = 2)
        val third = dao.getFeedPage(limit = 2, offset = 4)

        assertEquals(listOf(2, 2, 1), listOf(first.size, second.size, third.size))
        assertEquals(5, (first + second + third).map { it.id }.toSet().size)
    }

    @Test
    fun assetsWithoutPhashAreQueuedUntilHashed() = runTest {
        val id = dao.insert(asset())

        assertEquals(listOf(id), dao.getWithoutPhash(limit = 10).map { it.id })
        dao.setPhash(id, phash = -1L, isBlank = true)

        assertEquals(emptyList(), dao.getWithoutPhash(limit = 10))
        assertEquals(true, dao.getById(id)?.isBlank)
        assertEquals(-1L, dao.getById(id)?.phash)
    }

    @Test
    fun countFollowsInserts() = runTest {
        dao.insert(asset())
        dao.insert(asset())

        assertEquals(2, dao.observeCount().first())
    }
}
