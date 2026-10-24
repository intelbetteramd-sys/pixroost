package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import androidx.room3.Upsert
import app.pixroost.database.entity.YandexSharedRootEntity

/** Known shared roots of Yandex Disk, recognised by `resource_id` and not by path. */
@Dao
interface YandexSharedRootDao {
    @Upsert
    suspend fun upsert(root: YandexSharedRootEntity)

    @Query("SELECT * FROM yandex_shared_root WHERE source_id = :sourceId ORDER BY path")
    suspend fun getBySource(sourceId: Long): List<YandexSharedRootEntity>
}
