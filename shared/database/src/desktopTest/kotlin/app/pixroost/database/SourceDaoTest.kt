package app.pixroost.database

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SourceDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.sourceDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun insertedSourceIsReadBackWithDefaults() = runTest {
        val id = dao.insert(source(title = "Dropbox"))

        val stored = dao.getById(id)

        assertEquals("Dropbox", stored?.title)
        assertEquals("{}", stored?.configJson)
        assertEquals(false, stored?.isTrusted)
    }

    @Test
    fun deletedSourceIsGone() = runTest {
        val id = dao.insert(source())

        dao.delete(checkNotNull(dao.getById(id)))

        assertNull(dao.getById(id))
    }

    @Test
    fun observedListFollowsInserts() = runTest {
        dao.insert(source(title = "A"))
        val both = async { dao.observeAll().first { it.size == 2 } }

        dao.insert(source(title = "B"))

        assertEquals(listOf("A", "B"), both.await().map { it.title })
    }
}
