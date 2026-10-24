package app.pixroost.database

import app.pixroost.database.entity.DeviceEntity
import app.pixroost.database.model.DevicePlatform
import app.pixroost.database.model.DeviceRole
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeviceDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.deviceDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun deviceKeepsItsKeyAndDefaults() = runTest {
        dao.upsert(device("pc-1"))

        val stored = checkNotNull(dao.getById("pc-1"))

        assertContentEquals(hash(seed = 3), stored.publicKey)
        assertEquals(true, stored.archiveIsTrusted)
        assertEquals(false, stored.isDefaultArchive)
        assertNull(stored.revokedAt)
    }

    @Test
    fun upsertReplacesTheRowOfTheSameDevice() = runTest {
        dao.upsert(device("pc-1", name = "Old"))
        dao.upsert(device("pc-1", name = "New"))

        assertEquals("New", dao.getById("pc-1")?.name)
    }

    @Test
    fun revokedDeviceKeepsItsRow() = runTest {
        dao.upsert(device("phone-1"))

        dao.markRevoked("phone-1", revokedAt = 700)

        assertEquals(700L, dao.getById("phone-1")?.revokedAt)
    }

    private fun device(id: String, name: String = "PC") = DeviceEntity(
        id = id,
        name = name,
        platform = DevicePlatform.WINDOWS,
        role = DeviceRole.DESKTOP,
        publicKey = hash(seed = 3),
        permissions = "UPLOAD,BROWSE",
        pairedAt = 1,
    )
}
