package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import app.pixroost.database.entity.PhashBandEntity

/** The pHash band index. The candidates it returns still need the exact Hamming distance check. */
@Dao
interface PhashBandDao {
    @Insert
    suspend fun insertAll(bands: List<PhashBandEntity>)

    @Query("DELETE FROM phash_band WHERE asset_id = :assetId")
    suspend fun deleteForAsset(assetId: Long)

    /** Assets that share at least one whole band with the given band values (index = band number). */
    @Query(
        "SELECT DISTINCT asset_id FROM phash_band WHERE " +
            "(band = 0 AND value = :band0) OR (band = 1 AND value = :band1) OR (band = 2 AND value = :band2) OR " +
            "(band = 3 AND value = :band3) OR (band = 4 AND value = :band4)",
    )
    suspend fun findCandidateAssetIds(band0: Int, band1: Int, band2: Int, band3: Int, band4: Int): List<Long>
}
