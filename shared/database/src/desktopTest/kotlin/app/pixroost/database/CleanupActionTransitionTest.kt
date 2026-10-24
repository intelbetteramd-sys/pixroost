package app.pixroost.database

import app.pixroost.database.model.CleanupStatus
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

/** Status changes of the cleanup journal that must be refused: they would rewrite what really happened. */
class CleanupActionTransitionTest {
    private val temporary = TemporaryDatabase()
    private val database = temporary.database
    private val dao = database.cleanupActionDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun doneEntryCannotBecomeFailedRefusedOrUnrecoverable() = runTest {
        val id = database.insertPlannedCleanupAction()
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
        val restored = database.insertPlannedCleanupAction()
        dao.markDone(restored, executedAt = 500, trashKey = "trash/1", trashExpiresAt = null)
        dao.markRestored(restored, restoredAt = 600)
        val refused = database.insertPlannedCleanupAction()
        dao.markRefused(refused, denialReason = "no")

        assertEquals(0, dao.markDone(restored, executedAt = 900, trashKey = null, trashExpiresAt = null))
        assertEquals(0, dao.markDone(refused, executedAt = 900, trashKey = null, trashExpiresAt = null))

        assertEquals("trash/1", dao.getById(restored)?.trashKey)
        assertEquals(CleanupStatus.REFUSED, dao.getById(refused)?.status)
    }

    @Test
    fun onlyDoneEntryCanBeRestored() = runTest {
        val planned = database.insertPlannedCleanupAction()

        assertEquals(0, dao.markRestored(planned, restoredAt = 600))
        assertEquals(CleanupStatus.PLANNED, dao.getById(planned)?.status)
    }
}
