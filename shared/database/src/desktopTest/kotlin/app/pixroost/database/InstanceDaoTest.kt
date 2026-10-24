package app.pixroost.database

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class InstanceDaoTest {
    private val temporary = TemporaryDatabase()
    private val database = temporary.database
    private val dao = database.instanceDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun copyIsFoundBySourceAndRemoteKey() = runTest {
        val sourceId = database.sourceDao().insert(source())
        val assetId = database.assetDao().insert(asset())
        val id = dao.insert(instance(assetId, sourceId, remoteKey = "a/b.jpg"))

        assertEquals(id, dao.findBySourceAndKey(sourceId, "a/b.jpg")?.id)
    }

    @Test
    fun sameKeyInTheSameSourceIsRejected() = runTest {
        val sourceId = database.sourceDao().insert(source())
        val assetId = database.assetDao().insert(asset())
        dao.insert(instance(assetId, sourceId, remoteKey = "a.jpg"))

        assertFailsWith<SQLiteException> { dao.insert(instance(assetId, sourceId, remoteKey = "a.jpg")) }
    }

    @Test
    fun exactCopiesInDifferentSourcesAreFoundByHash() = runTest {
        val first = database.sourceDao().insert(source(title = "Dropbox"))
        val second = database.sourceDao().insert(source(title = "Yandex Disk"))
        val assetId = database.assetDao().insert(asset())
        dao.insert(instance(assetId, first, sha256 = hash(seed = 7)))
        dao.insert(instance(assetId, second, sha256 = hash(seed = 7)))
        dao.insert(instance(assetId, second, remoteKey = "other.jpg", sha256 = hash(seed = 8)))

        assertEquals(setOf(first, second), dao.findBySha256(hash(seed = 7)).map { it.sourceId }.toSet())
        assertEquals(3, dao.getByAsset(assetId).size)
    }

    @Test
    fun copyOfAMissingAssetIsRejectedBecauseForeignKeysAreOn() = runTest {
        val sourceId = database.sourceDao().insert(source())

        assertFailsWith<SQLiteException> { dao.insert(instance(assetId = 999, sourceId = sourceId)) }
    }

    @Test
    fun disconnectingASourceRemovesItsCopiesButKeepsTheAsset() = runTest {
        val sourceId = database.sourceDao().insert(source())
        val assetId = database.assetDao().insert(asset())
        dao.insert(instance(assetId, sourceId))

        database.sourceDao().delete(checkNotNull(database.sourceDao().getById(sourceId)))

        assertEquals(0, dao.countBySource(sourceId))
        assertNotNull(database.assetDao().getById(assetId))
    }
}
