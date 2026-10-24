package app.pixroost.database

import app.pixroost.database.entity.ArchiveIndexEntity
import app.pixroost.database.entity.DeviceEntity
import app.pixroost.database.entity.TransferItemEntity
import app.pixroost.database.entity.TransferJobEntity
import app.pixroost.database.model.DevicePlatform
import app.pixroost.database.model.DeviceRole
import app.pixroost.database.model.TransferItemStatus
import app.pixroost.database.model.TransferJobKind
import app.pixroost.database.model.TransferJobStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TransferDaoTest {
    private val temporary = TemporaryDatabase()
    private val database = temporary.database
    private val dao = database.transferDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun jobProgressesThroughItsItems() = runTest {
        val (jobId, instanceId) = queueOneFile()
        val itemId = dao.getItems(jobId).single().id

        dao.setJobStatus(jobId, TransferJobStatus.RUNNING)
        dao.setItemProgress(itemId, bytesSent = 50, uploadId = "up-1")
        dao.finishItem(itemId, TransferItemStatus.DONE, error = null, savedPath = "2025/IMG_1.jpg", finishedAt = 9)

        val item = dao.getItems(jobId).single()
        assertEquals(instanceId, item.instanceId)
        assertEquals(TransferItemStatus.DONE, item.status)
        assertEquals(50L, item.bytesSent)
        assertEquals("up-1", item.uploadId)
        assertEquals("2025/IMG_1.jpg", item.savedPath)
        assertEquals(TransferJobStatus.RUNNING, dao.observeJobs().first().single().status)
    }

    @Test
    fun deviceKeepsItsArchiveIndex() = runTest {
        database.deviceDao().upsert(device())
        database.archiveIndexDao().upsertAll(
            listOf(ArchiveIndexEntity("pc-1", hash(seed = 5), savedPath = "a.jpg")),
        )

        assertEquals(true, database.archiveIndexDao().contains("pc-1", hash(seed = 5)))
        assertEquals(false, database.archiveIndexDao().contains("pc-1", hash(seed = 6)))
        assertEquals("a.jpg", database.archiveIndexDao().getSavedPath("pc-1", hash(seed = 5)))
    }

    private suspend fun queueOneFile(): Pair<Long, Long> {
        database.deviceDao().upsert(device())
        val sourceId = database.sourceDao().insert(source())
        val assetId = database.assetDao().insert(asset())
        val instanceId = database.instanceDao().insert(instance(assetId, sourceId))
        val jobId = dao.insertJob(
            TransferJobEntity(
                deviceId = "pc-1",
                kind = TransferJobKind.MANUAL,
                status = TransferJobStatus.QUEUED,
                createdAt = 1,
            ),
        )
        dao.insertItems(
            listOf(TransferItemEntity(jobId = jobId, instanceId = instanceId, status = TransferItemStatus.QUEUED)),
        )
        return jobId to instanceId
    }

    private fun device() = DeviceEntity(
        id = "pc-1",
        name = "PC",
        platform = DevicePlatform.WINDOWS,
        role = DeviceRole.DESKTOP,
        publicKey = hash(seed = 3),
        permissions = "UPLOAD",
        pairedAt = 1,
    )
}
