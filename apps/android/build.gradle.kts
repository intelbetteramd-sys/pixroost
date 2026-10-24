plugins {
    alias(libs.plugins.pixroost.android.application)
}

base {
    // APK files are named pixroost-debug.apk and so on, which is easier to find in Telegram.
    archivesName = "pixroost"
}

android {
    defaultConfig {
        applicationId = "app.pixroost"
        // Spike S-06: installs next to the regular app and next to the S-02 spike.
        applicationIdSuffix = ".spike.phash"
        versionCode = 1
        versionName = "0.1.0-spike.s06"
    }
}

dependencies {
    implementation(project(":shared:core"))
    implementation(project(":shared:designsystem"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
}
