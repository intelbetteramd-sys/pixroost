package app.pixroost.database.dao

import androidx.room3.Dao
import androidx.room3.Query
import app.pixroost.database.entity.YandexSharedRootEntity

/** Known shared roots of Yandex Disk, recognised by `resource_id` and not by path. */
@Dao
interface YandexSharedRootDao {
    /** Adds the root, or moves its path and last-seen time; `first_seen_at` keeps the first sighting. */
    @Query(
        "INSERT INTO yandex_shared_root (source_id, resource_id, path, first_seen_at, last_seen_at) " +
            "VALUES (:sourceId, :resourceId, :path, :seenAt, :seenAt) " +
            "ON CONFLICT(source_id, resource_id) DO UPDATE SET path = excluded.path, " +
            "last_seen_at = excluded.last_seen_at",
    )
    suspend fun upsert(sourceId: Long, resourceId: String, path: String, seenAt: Long)

    @Query("SELECT * FROM yandex_shared_root WHERE source_id = :sourceId ORDER BY path")
    suspend fun getBySource(sourceId: Long): List<YandexSharedRootEntity>
}
