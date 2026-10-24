package app.pixroost.database

import app.pixroost.database.model.CleanupStatus
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CleanupActionDaoTest {
    private val temporary = TemporaryDatabase()
    private val database = temporary.database
    private val dao = database.cleanupActionDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun journalEntryOutlivesDisconnectedSourceAndKeepsItsEvidence() = runTest {
        val dropbox = database.sourceDao().insert(source(title = "Dropbox"))
        val yandex = database.sourceDao().insert(source(title = "Yandex Disk"))
        val assetId = database.assetDao().insert(asset())
        val deleted = database.instanceDao().insert(instance(assetId, dropbox, sha256 = hash(seed = 1)))
        val keeper = database.instanceDao().insert(instance(assetId, yandex, sha256 = hash(seed = 1)))
        val id = dao.insert(cleanupAction(instanceId = deleted, keeperInstanceId = keeper))

        database.sourceDao().delete(checkNotNull(database.sourceDao().getById(dropbox)))
        database.sourceDao().delete(checkNotNull(database.sourceDao().getById(yandex)))

        val entry = assertNotNull(dao.getById(id))
        assertNull(entry.instanceId)
        assertNull(entry.keeperInstanceId)
        assertEquals("Dropbox", entry.sourceTitle)
        assertEquals("Yandex Disk", entry.keeperTitle)
        assertEquals("IMG_1.jpg", entry.remoteKey)
        assertContentEquals(hash(seed = 1), entry.deletedSha256)
        assertContentEquals(hash(seed = 1), entry.keeperSha256)
    }

    @Test
    fun trashedActionRecordsWhenItWasDoneAndHowToRestoreIt() = runTest {
        val id = database.insertPlannedCleanupAction()

        assertEquals(1, dao.markDone(id, executedAt = 500, trashKey = "trash/1", trashExpiresAt = null))

        val entry = checkNotNull(dao.getById(id))
        assertEquals(CleanupStatus.DONE, entry.status)
        assertEquals(500L, entry.executedAt)
        assertEquals("trash/1", entry.trashKey)
        // NULL means "the source does not say how long it keeps the file"; the observer relies on it.
        assertNull(entry.trashExpiresAt)
    }

    @Test
    fun refusalKeepsTheReasonAndTheEvidence() = runTest {
        val id = database.insertPlannedCleanupAction()

        assertEquals(1, dao.markRefused(id, denialReason = "keeper hash differs"))

        val entry = checkNotNull(dao.getById(id))
        assertEquals(CleanupStatus.REFUSED, entry.status)
        assertEquals("keeper hash differs", entry.denialReason)
        assertContentEquals(hash(seed = 1), entry.deletedSha256)
    }

    @Test
    fun failureAndUnrecoverableDeletionAreRecorded() = runTest {
        val failed = database.insertPlannedCleanupAction()
        val unrecoverable = database.insertPlannedCleanupAction()

        assertEquals(1, dao.markFailed(failed, error = "timeout"))
        assertEquals(1, dao.markUnrecoverable(unrecoverable, executedAt = 700, error = "not in the recycle bin"))

        assertEquals(CleanupStatus.FAILED, dao.getById(failed)?.status)
        val entry = checkNotNull(dao.getById(unrecoverable))
        assertEquals(CleanupStatus.UNRECOVERABLE, entry.status)
        assertEquals(700L, entry.executedAt)
    }

    @Test
    fun restoredActionKeepsItsOriginalExecutionTime() = runTest {
        val id = database.insertPlannedCleanupAction()
        dao.markDone(id, executedAt = 500, trashKey = "trash/1", trashExpiresAt = null)

        assertEquals(1, dao.markRestored(id, restoredAt = 600))

        val entry = checkNotNull(dao.getById(id))
        assertEquals(CleanupStatus.RESTORED, entry.status)
        assertEquals(500L, entry.executedAt)
        assertEquals(600L, entry.restoredAt)
    }

    @Test
    fun lostKeeperWarningIsRecordedOnce() = runTest {
        val id = database.insertPlannedCleanupAction()

        assertEquals(1, dao.markKeeperLostNotified(id, notifiedAt = 800))
        assertEquals(0, dao.markKeeperLostNotified(id, notifiedAt = 900))

        assertEquals(800L, dao.getById(id)?.keeperLostNotifiedAt)
    }

    @Test
    fun actionsOfOnePlanAreListedTogether() = runTest {
        database.insertPlannedCleanupAction()
        database.insertPlannedCleanupAction()

        assertEquals(2, dao.getByPlan("plan-1").size)
        assertEquals(0, dao.getByPlan("other").size)
    }
}
