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
        val id = insertPlannedAction()

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
        val id = insertPlannedAction()

        assertEquals(1, dao.markRefused(id, denialReason = "keeper hash differs"))

        val entry = checkNotNull(dao.getById(id))
        assertEquals(CleanupStatus.REFUSED, entry.status)
        assertEquals("keeper hash differs", entry.denialReason)
        assertContentEquals(hash(seed = 1), entry.deletedSha256)
    }

    @Test
    fun failureAndUnrecoverableDeletionAreRecorded() = runTest {
        val failed = insertPlannedAction()
        val unrecoverable = insertPlannedAction()

        assertEquals(1, dao.markFailed(failed, error = "timeout"))
        assertEquals(1, dao.markUnrecoverable(unrecoverable, executedAt = 700, error = "not in the recycle bin"))

        assertEquals(CleanupStatus.FAILED, dao.getById(failed)?.status)
        val entry = checkNotNull(dao.getById(unrecoverable))
        assertEquals(CleanupStatus.UNRECOVERABLE, entry.status)
        assertEquals(700L, entry.executedAt)
    }

    @Test
    fun restoredActionKeepsItsOriginalExecutionTime() = runTest {
        val id = insertPlannedAction()
        dao.markDone(id, executedAt = 500, trashKey = "trash/1", trashExpiresAt = null)

        assertEquals(1, dao.markRestored(id, restoredAt = 600))

        val entry = checkNotNull(dao.getById(id))
        assertEquals(CleanupStatus.RESTORED, entry.status)
        assertEquals(500L, entry.executedAt)
        assertEquals(600L, entry.restoredAt)
    }

    @Test
    fun doneEntryCannotBecomeFailedRefusedOrUnrecoverable() = runTest {
        val id = insertPlannedAction()
        dao.markDone(id, executedAt = 500, trashKey = "trash/1", trashExpiresAt = null)

        assertEquals(0, dao.markFailed(id, error = "late"))
        assertEquals(0, dao.markRefused(id, denialReason = "late"))
        assertEquals(0, dao.markUnrecoverable(id, executedAt = 900, error = "late"))
        assertEquals(0, dao.markDone(id, executedAt = 900, trashKey = null, trashExpiresAt = null))

        val entry = checkNotNull(dao.getById(id))
        assertEquals(CleanupStatus.DONE, entry.status)
        assertEquals(500L, entry.executedAt)
        assertEquals("trash/1", entry.trashKey)
    }

    @Test
    fun restoredOrRefusedEntryCannotGoBackToDone() = runTest {
        val restored = insertPlannedAction()
        dao.markDone(restored, executedAt = 500, trashKey = "trash/1", trashExpiresAt = null)
        dao.markRestored(restored, restoredAt = 600)
        val refused = insertPlannedAction()
        dao.markRefused(refused, denialReason = "no")

        assertEquals(0, dao.markDone(restored, executedAt = 900, trashKey = null, trashExpiresAt = null))
        assertEquals(0, dao.markDone(refused, executedAt = 900, trashKey = null, trashExpiresAt = null))

        assertEquals("trash/1", dao.getById(restored)?.trashKey)
        assertEquals(CleanupStatus.REFUSED, dao.getById(refused)?.status)
    }

    @Test
    fun onlyDoneEntryCanBeRestored() = runTest {
        val planned = insertPlannedAction()

        assertEquals(0, dao.markRestored(planned, restoredAt = 600))
        assertEquals(CleanupStatus.PLANNED, dao.getById(planned)?.status)
    }

    @Test
    fun lostKeeperWarningIsRecordedOnce() = runTest {
        val id = insertPlannedAction()

        assertEquals(1, dao.markKeeperLostNotified(id, notifiedAt = 800))
        assertEquals(0, dao.markKeeperLostNotified(id, notifiedAt = 900))

        assertEquals(800L, dao.getById(id)?.keeperLostNotifiedAt)
    }

    @Test
    fun actionsOfOnePlanAreListedTogether() = runTest {
        insertPlannedAction()
        insertPlannedAction()

        assertEquals(2, dao.getByPlan("plan-1").size)
        assertEquals(0, dao.getByPlan("other").size)
    }

    private suspend fun insertPlannedAction(): Long {
        val sourceId = database.sourceDao().insert(source())
        val assetId = database.assetDao().insert(asset())
        val copy = database.instanceDao().insert(instance(assetId, sourceId, remoteKey = "k${System.nanoTime()}"))
        return dao.insert(cleanupAction(instanceId = copy, keeperInstanceId = copy))
    }
}
