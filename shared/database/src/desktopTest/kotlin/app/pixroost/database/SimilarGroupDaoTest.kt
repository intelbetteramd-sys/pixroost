package app.pixroost.database

import app.pixroost.database.entity.SimilarGroupEntity
import app.pixroost.database.entity.SimilarMemberEntity
import app.pixroost.database.model.SimilarGroupKind
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SimilarGroupDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.similarGroupDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun dismissedGroupLeavesTheActiveList() = runTest {
        val kept = dao.insertGroup(SimilarGroupEntity(kind = SimilarGroupKind.SIMILAR, createdAt = 1))
        val dismissed = dao.insertGroup(SimilarGroupEntity(kind = SimilarGroupKind.SAME_SHOT, createdAt = 2))

        dao.dismiss(dismissed)

        assertEquals(listOf(kept), dao.observeActiveGroups().first().map { it.id })
    }

    @Test
    fun membersAreStoredWithTheirDistance() = runTest {
        val group = dao.insertGroup(SimilarGroupEntity(kind = SimilarGroupKind.SIMILAR, createdAt = 1))
        val first = temporary.database.assetDao().insert(asset())
        val second = temporary.database.assetDao().insert(asset())

        dao.insertMembers(
            listOf(
                SimilarMemberEntity(groupId = group, assetId = first, distance = 0),
                SimilarMemberEntity(groupId = group, assetId = second, distance = 6),
            ),
        )

        assertEquals(listOf(0, 6), dao.getMembers(group).map { it.distance })
    }
}
