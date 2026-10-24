package app.pixroost.database

import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class YandexSharedRootDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.yandexSharedRootDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun rootIsRecognisedByResourceIdAndItsPathIsUpdated() = runTest {
        val sourceId = temporary.database.sourceDao().insert(source())
        dao.upsert(sourceId, "res-1", path = "/Shared/Old", seenAt = 1)

        dao.upsert(sourceId, "res-1", path = "/Shared/New", seenAt = 2)

        val roots = dao.getBySource(sourceId)
        assertEquals(listOf("/Shared/New"), roots.map { it.path })
        assertEquals(2L, roots.single().lastSeenAt)
        assertEquals(1L, roots.single().firstSeenAt)
    }

    @Test
    fun rootsGoWithTheirSource() = runTest {
        val sourceId = temporary.database.sourceDao().insert(source())
        dao.upsert(sourceId, "res-1", path = "/Shared", seenAt = 1)

        temporary.database.sourceDao().delete(checkNotNull(temporary.database.sourceDao().getById(sourceId)))

        assertEquals(emptyList(), dao.getBySource(sourceId))
    }
}
