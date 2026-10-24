package app.pixroost.database

import androidx.sqlite.SQLiteException
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DeviceDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.deviceDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun deviceKeepsItsKeyAndDefaults() = runTest {
        dao.insert(device("pc-1"))

        val stored = checkNotNull(dao.getById("pc-1"))

        assertContentEquals(hash(seed = 3), stored.publicKey)
        assertEquals(true, stored.archiveIsTrusted)
        assertEquals(false, stored.isDefaultArchive)
        assertNull(stored.revokedAt)
    }

    @Test
    fun pairingAnExistingIdAgainIsRejected() = runTest {
        dao.insert(device("pc-1"))

        assertFailsWith<SQLiteException> { dao.insert(device("pc-1", name = "Again")) }
    }

    @Test
    fun revocationSurvivesRenameAndLastSeenUpdates() = runTest {
        dao.insert(device("phone-1"))

        assertEquals(1, dao.markRevoked("phone-1", revokedAt = 700))
        dao.rename("phone-1", "Renamed")
        dao.updateLastSeen("phone-1", seenAt = 800, address = "192.168.0.2")

        val stored = checkNotNull(dao.getById("phone-1"))
        assertEquals(700L, stored.revokedAt)
        assertEquals("Renamed", stored.name)
    }

    @Test
    fun secondRevocationKeepsTheFirstTime() = runTest {
        dao.insert(device("phone-1"))
        dao.markRevoked("phone-1", revokedAt = 700)

        assertEquals(0, dao.markRevoked("phone-1", revokedAt = 900))
        assertEquals(700L, dao.getById("phone-1")?.revokedAt)
    }
}
