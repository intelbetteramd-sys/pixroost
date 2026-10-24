package app.pixroost.android.spike.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

/** Permissions to ask for on this Android version. */
fun galleryPermissions(): Array<String> = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ->
        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)

    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
    else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
}

/** The access granted right now. */
fun galleryAccess(context: Context): GalleryAccess {
    val granted = { permission: String ->
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }
    val sdk = Build.VERSION.SDK_INT
    return when {
        sdk >= Build.VERSION_CODES.TIRAMISU && granted(Manifest.permission.READ_MEDIA_IMAGES) -> GalleryAccess.Full
        sdk >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && granted(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) ->
            GalleryAccess.Partial

        sdk < Build.VERSION_CODES.TIRAMISU && granted(Manifest.permission.READ_EXTERNAL_STORAGE) -> GalleryAccess.Full
        else -> GalleryAccess.None
    }
}
