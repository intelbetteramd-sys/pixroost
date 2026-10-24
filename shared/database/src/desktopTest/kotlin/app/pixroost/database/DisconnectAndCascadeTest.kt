package app.pixroost.database

import app.pixroost.database.entity.ArchiveIndexEntity
import app.pixroost.database.entity.PhashBandEntity
import app.pixroost.database.entity.SimilarGroupEntity
import app.pixroost.database.entity.SimilarMemberEntity
import app.pixroost.database.entity.TransferItemEntity
import app.pixroost.database.entity.TransferJobEntity
import app.pixroost.database.model.SimilarGroupKind
import app.pixroost.database.model.SourceKind
import app.pixroost.database.model.TransferItemStatus
import app.pixroost.database.model.TransferJobKind
import app.pixroost.database.model.TransferJobStatus
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/** Foreign key actions of the schema, one table pair at a time and for a whole source at once. */
class DisconnectAndCascadeTest {
    private val temporary = TemporaryDatabase()
    private val database = temporary.database

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun deletingAnAssetRemovesItsBandsAndGroupMemberships() = runTest {
        val assetId = database.assetDao().insert(asset())
        database.phashBandDao().insertAll(listOf(PhashBandEntity(assetId = assetId, band = 0, value = 7)))
        val group = database.similarGroupDao()
            .insertGroup(SimilarGroupEntity(kind = SimilarGroupKind.SIMILAR, createdAt = 1))
        database.similarGroupDao().insertMembers(listOf(SimilarMemberEntity(group, assetId, distance = 0)))

        temporary.execute("DELETE FROM asset WHERE id = $assetId")

        assertEquals(emptyList(), database.phashBandDao().findCandidateAssetIds(7, 0, 0, 0, 0))
        assertEquals(emptyList(), database.similarGroupDao().getMembers(group))
    }

    @Test
    fun deletingAGroupRemovesItsMembersButNotTheAssets() = runTest {
        val assetId = database.assetDao().insert(asset())
        val group = database.similarGroupDao()
            .insertGroup(SimilarGroupEntity(kind = SimilarGroupKind.SAME_SHOT, createdAt = 1))
        database.similarGroupDao().insertMembers(listOf(SimilarMemberEntity(group, assetId, distance = 0)))

        temporary.execute("DELETE FROM similar_group WHERE id = $group")

        assertEquals(emptyList(), database.similarGroupDao().getMembers(group))
        assertNotNull(database.assetDao().getById(assetId))
    }

    @Test
    fun deletingAJobRemovesItsItems() = runTest {
        val (jobId, _) = queueFile()

        temporary.execute("DELETE FROM transfer_job WHERE id = $jobId")

        assertEquals(emptyList(), database.transferDao().getItems(jobId))
    }

    @Test
    fun deletingADeviceRemovesItsArchiveIndex() = runTest {
        database.deviceDao().insert(device())
        database.archiveIndexDao().upsertAll(listOf(ArchiveIndexEntity("pc-1", hash(seed = 5), savedPath = "a.jpg")))

        temporary.execute("DELETE FROM device WHERE id = 'pc-1'")

        assertEquals(false, database.archiveIndexDao().contains("pc-1", hash(seed = 5)))
    }

    @Test
    fun bestCopyIsClearedWhenItsSourceIsDisconnected() = runTest {
        val sourceId = database.sourceDao().insert(source())
        val assetId = database.assetDao().insert(asset())
        val copy = database.instanceDao().insert(instance(assetId, sourceId))
        database.assetDao().setBestInstance(assetId, copy)

        database.sourceDao().delete(checkNotNull(database.sourceDao().getById(sourceId)))

        assertNull(database.assetDao().getById(assetId)?.bestInstanceId)
    }

    @Test
    fun disconnectingASourceWithAFullSetOfChildRowsWorks() = runTest {
        val dropbox = database.sourceDao().insert(source(title = "Dropbox"))
        val yandex = database.sourceDao().insert(source(title = "Yandex Disk", kind = SourceKind.YANDEX_DISK))
        val assetId = database.assetDao().insert(asset())
        val deleted = database.instanceDao().insert(instance(assetId, dropbox, sha256 = hash(seed = 1)))
        val keeper = database.instanceDao().insert(instance(assetId, yandex, sha256 = hash(seed = 1)))
        database.assetDao().setBestInstance(assetId, deleted)
        database.yandexSharedRootDao().upsert(dropbox, "res-1", path = "/Shared", seenAt = 1)
        val (jobId, _) = queueFile(sourceId = dropbox, assetId = assetId)
        val action = database.cleanupActionDao().insert(cleanupAction(instanceId = deleted, keeperInstanceId = keeper))

        database.sourceDao().delete(checkNotNull(database.sourceDao().getById(dropbox)))

        assertEquals(0, database.instanceDao().countBySource(dropbox))
        assertEquals(emptyList(), database.transferDao().getItems(jobId))
        assertEquals(emptyList(), database.yandexSharedRootDao().getBySource(dropbox))
        assertNull(database.assetDao().getById(assetId)?.bestInstanceId)
        val journal = checkNotNull(database.cleanupActionDao().getById(action))
        assertNull(journal.instanceId)
        assertEquals(keeper, journal.keeperInstanceId)
        assertEquals("Dropbox", journal.sourceTitle)
    }

    private suspend fun queueFile(sourceId: Long? = null, assetId: Long? = null): Pair<Long, Long> {
        database.deviceDao().insert(device())
        val source = sourceId ?: database.sourceDao().insert(source())
        val asset = assetId ?: database.assetDao().insert(asset())
        val copy = database.instanceDao().insert(instance(asset, source, remoteKey = "queued.jpg"))
        val jobId = database.transferDao().insertJob(
            TransferJobEntity(
                deviceId = "pc-1",
                kind = TransferJobKind.MANUAL,
                status = TransferJobStatus.QUEUED,
                createdAt = 1,
            ),
        )
        database.transferDao().insertItems(
            listOf(TransferItemEntity(jobId = jobId, instanceId = copy, status = TransferItemStatus.QUEUED)),
        )
        return jobId to copy
    }
}
