package app.pixroost.database

import app.pixroost.database.entity.AssetEntity
import app.pixroost.database.entity.CleanupActionEntity
import app.pixroost.database.entity.DeviceEntity
import app.pixroost.database.entity.InstanceEntity
import app.pixroost.database.entity.SourceEntity
import app.pixroost.database.model.CleanupActionType
import app.pixroost.database.model.CleanupStatus
import app.pixroost.database.model.DevicePlatform
import app.pixroost.database.model.DeviceRole
import app.pixroost.database.model.InstanceQuality
import app.pixroost.database.model.InstanceState
import app.pixroost.database.model.KeeperKind
import app.pixroost.database.model.MediaType
import app.pixroost.database.model.SourceKind
import app.pixroost.database.model.SourceStatus

fun source(title: String = "Dropbox", kind: SourceKind = SourceKind.DROPBOX) =
    SourceEntity(kind = kind, title = title, status = SourceStatus.OK)

fun asset(takenAt: Long? = null) = AssetEntity(mediaType = MediaType.PHOTO, takenAt = takenAt)

fun instance(assetId: Long, sourceId: Long, remoteKey: String = "IMG_1.jpg", sha256: ByteArray? = null) =
    InstanceEntity(
        assetId = assetId,
        sourceId = sourceId,
        remoteKey = remoteKey,
        sha256 = sha256,
        quality = InstanceQuality.ORIGINAL,
        state = InstanceState.PRESENT,
        lastSeenAt = 0,
    )

fun device(id: String = "pc-1", name: String = "PC") = DeviceEntity(
    id = id,
    name = name,
    platform = DevicePlatform.WINDOWS,
    role = DeviceRole.DESKTOP,
    publicKey = hash(seed = 3),
    permissions = "UPLOAD,BROWSE",
    pairedAt = 1,
)

fun hash(seed: Byte) = ByteArray(size = 32) { seed }

fun cleanupAction(instanceId: Long, keeperInstanceId: Long) = CleanupActionEntity(
    planId = "plan-1",
    instanceId = instanceId,
    fileName = "IMG_1.jpg",
    sourceKind = SourceKind.DROPBOX,
    sourceTitle = "Dropbox",
    remoteKey = "IMG_1.jpg",
    deletedSha256 = hash(seed = 1),
    keeperKind = KeeperKind.INSTANCE,
    keeperInstanceId = keeperInstanceId,
    keeperTitle = "Yandex Disk",
    keeperSha256 = hash(seed = 1),
    action = CleanupActionType.TRASH,
    status = CleanupStatus.PLANNED,
)

suspend fun PixroostDatabase.insertPlannedCleanupAction(): Long {
    val sourceId = sourceDao().insert(source())
    val assetId = assetDao().insert(asset())
    val copy = instanceDao().insert(instance(assetId, sourceId, remoteKey = "k${System.nanoTime()}"))
    return cleanupActionDao().insert(cleanupAction(instanceId = copy, keeperInstanceId = copy))
}
