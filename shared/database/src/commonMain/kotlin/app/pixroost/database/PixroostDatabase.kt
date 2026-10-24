package app.pixroost.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import app.pixroost.database.dao.ArchiveIndexDao
import app.pixroost.database.dao.AssetDao
import app.pixroost.database.dao.CleanupActionDao
import app.pixroost.database.dao.ConnectionLogDao
import app.pixroost.database.dao.DeviceDao
import app.pixroost.database.dao.InstanceDao
import app.pixroost.database.dao.PhashBandDao
import app.pixroost.database.dao.SimilarGroupDao
import app.pixroost.database.dao.SourceDao
import app.pixroost.database.dao.TransferDao
import app.pixroost.database.dao.YandexSharedRootDao
import app.pixroost.database.entity.ArchiveIndexEntity
import app.pixroost.database.entity.AssetEntity
import app.pixroost.database.entity.CleanupActionEntity
import app.pixroost.database.entity.ConnectionLogEntity
import app.pixroost.database.entity.DeviceEntity
import app.pixroost.database.entity.InstanceEntity
import app.pixroost.database.entity.PhashBandEntity
import app.pixroost.database.entity.SimilarGroupEntity
import app.pixroost.database.entity.SimilarMemberEntity
import app.pixroost.database.entity.SourceEntity
import app.pixroost.database.entity.TransferItemEntity
import app.pixroost.database.entity.TransferJobEntity
import app.pixroost.database.entity.YandexSharedRootEntity

/**
 * The local database of one device. Schema: docs/architecture/data-model.md.
 *
 * After the first release the schema changes only through migrations ([DatabaseMigrations]); each version
 * is exported to `shared/database/schemas/` and the export is committed together with the change.
 */
@Database(
    entities = [
        SourceEntity::class,
        AssetEntity::class,
        InstanceEntity::class,
        YandexSharedRootEntity::class,
        PhashBandEntity::class,
        SimilarGroupEntity::class,
        SimilarMemberEntity::class,
        DeviceEntity::class,
        ConnectionLogEntity::class,
        TransferJobEntity::class,
        TransferItemEntity::class,
        ArchiveIndexEntity::class,
        CleanupActionEntity::class,
    ],
    version = DatabaseConstants.VERSION,
)
@ConstructedBy(PixroostDatabaseConstructor::class)
abstract class PixroostDatabase : RoomDatabase() {
    abstract fun sourceDao(): SourceDao

    abstract fun assetDao(): AssetDao

    abstract fun instanceDao(): InstanceDao

    abstract fun yandexSharedRootDao(): YandexSharedRootDao

    abstract fun phashBandDao(): PhashBandDao

    abstract fun similarGroupDao(): SimilarGroupDao

    abstract fun deviceDao(): DeviceDao

    abstract fun connectionLogDao(): ConnectionLogDao

    abstract fun transferDao(): TransferDao

    abstract fun archiveIndexDao(): ArchiveIndexDao

    abstract fun cleanupActionDao(): CleanupActionDao
}
