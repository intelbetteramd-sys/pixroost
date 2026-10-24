package app.pixroost.database

import app.pixroost.database.entity.PhashBandEntity
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PhashBandDaoTest {
    private val temporary = TemporaryDatabase()
    private val dao = temporary.database.phashBandDao()

    @AfterTest
    fun tearDown() = temporary.close()

    @Test
    fun assetsSharingOneWholeBandAreCandidates() = runTest {
        val first = temporary.database.assetDao().insert(asset())
        val second = temporary.database.assetDao().insert(asset())
        val unrelated = temporary.database.assetDao().insert(asset())
        dao.insertAll(bands(first, 10, 20, 30, 40, 50))
        dao.insertAll(bands(second, 10, 21, 31, 41, 51))
        dao.insertAll(bands(unrelated, 11, 21, 31, 41, 51))

        val candidates = dao.findCandidateAssetIds(10, 20, 30, 40, 50)

        assertEquals(setOf(first, second), candidates.toSet())
    }

    @Test
    fun sameValueInAnotherBandIsNotACandidate() = runTest {
        val asset = temporary.database.assetDao().insert(asset())
        dao.insertAll(bands(asset, 1, 2, 3, 4, 5))

        assertEquals(emptyList(), dao.findCandidateAssetIds(2, 1, 4, 3, 6))
    }

    @Test
    fun deletedBandsNoLongerMatch() = runTest {
        val asset = temporary.database.assetDao().insert(asset())
        dao.insertAll(bands(asset, 1, 2, 3, 4, 5))

        dao.deleteForAsset(asset)

        assertEquals(emptyList(), dao.findCandidateAssetIds(1, 2, 3, 4, 5))
    }

    private fun bands(assetId: Long, vararg values: Int) =
        values.mapIndexed { band, value -> PhashBandEntity(assetId = assetId, band = band, value = value) }
}
