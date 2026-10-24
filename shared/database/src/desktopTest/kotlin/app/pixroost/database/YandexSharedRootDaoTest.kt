package app.pixroost.database

import app.pixroost.database.entity.YandexSharedRootEntity
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
        dao.upsert(YandexSharedRootEntity(sourceId, "res-1", path = "/Shared/Old", firstSeenAt = 1, lastSeenAt = 1))

        dao.upsert(YandexSharedRootEntity(sourceId, "res-1", path = "/Shared/New", firstSeenAt = 1, lastSeenAt = 2))

        val roots = dao.getBySource(sourceId)
        assertEquals(listOf("/Shared/New"), roots.map { it.path })
        assertEquals(2L, roots.single().lastSeenAt)
    }

    @Test
    fun rootsGoWithTheirSource() = runTest {
        val sourceId = temporary.database.sourceDao().insert(source())
        dao.upsert(YandexSharedRootEntity(sourceId, "res-1", path = "/Shared", firstSeenAt = 1, lastSeenAt = 1))

        temporary.database.sourceDao().delete(checkNotNull(temporary.database.sourceDao().getById(sourceId)))

        assertEquals(emptyList(), dao.getBySource(sourceId))
    }
}
